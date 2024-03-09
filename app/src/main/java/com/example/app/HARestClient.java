package com.example.app;

import com.example.app.dto.StateResponseDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@Component
public class HARestClient {

    @Value("#{new Integer('${region.id}')}")
    private Long regionId;

    @Value(value = "${ha.url}")
    private String haUrl;

    @Value(value = "${ha.webhook.covers.open.id}")
    private String webhookOpenId;

    @Value(value = "${ha.webhook.covers.close.id}")
    private String webhookCloseId;

    @Value(value = "${air-raid-alert.api.token}")
    private String airRaidAlertTokenApi;

    @Value(value = "${ha.rest.bearer-token}")
    private String haBearerToken;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Method makes calls for rest api of air raid alert state.
     * @return response body with specific code (
     * A - повітряна тривога активна в усій області
     * P - часткова тривога в районах чи громадах
     * N - немає інформації про повітряну тривогу)
     * or else error response
     */
    public ResponseEntity<String> getAirRaidAlertState() {
        try {
            String response = restTemplate.getForObject(constructUrl4AirRaidAlertApi(), String.class);
            log.info("Success got air raid alert state");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    public void manageWebhookCall(boolean isAlert) {
        if (isAlert) {
            StateResponseDto responseDto = getStateOfEntityId("cover.curtain_6bf2");
            if (responseDto.getState() != null && responseDto.getState().equals("opened")) {
                restTemplate.postForEntity(constructUrl4WebhookHAServer(HttpMethod.POST, false), "null", String.class);
                log.info("Webhook call was executed with success");
            }
        }
    }

    @SneakyThrows
    public StateResponseDto getStateOfEntityId(String entityId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(haBearerToken);
        ResponseEntity<StateResponseDto> response = restTemplate.exchange(RequestEntity.get(new URI(haUrl + "/api/states/" + entityId)).headers(headers).build(), StateResponseDto.class);
        if (response.getStatusCode().is4xxClientError()) {
            log.error("HA server is not responding or wrong entityId is provided");
        }
        return response.getBody();
    }


    private String constructUrl4WebhookHAServer(HttpMethod method, boolean open) {
        return haUrl + "/api/webhook/" + (open ? webhookOpenId : webhookCloseId);
    }

    private String constructUrl4AirRaidAlertApi() {
        return "https://api.alerts.in.ua/v1/iot/active_air_raid_alerts/" + regionId + ".json?token=" + airRaidAlertTokenApi;
    }
}
