package com.example.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Home assistant scheduler that will run periodically and send requests via {@link HARestClient}
 */
@Slf4j
@Component
public class HAScheduler implements Runnable{

    @Value("#{new Boolean('${app.settings.useStrictMode}')}")
    private boolean useStrictMode;

    @Autowired
    private HARestClient haRestClient;


    @Override
    public void run() {
        String response = haRestClient.getAirRaidAlertState().getBody();
        if (response == null) {
            log.error("Something went wrong while getting response about air-raid alerts");
        } else {
            haRestClient.manageWebhookCall(isAlert(response));
        }
    }

    private boolean isAlert(String code) {
        return code.equals("A") || (useStrictMode && code.equals("P"));
    }
}
