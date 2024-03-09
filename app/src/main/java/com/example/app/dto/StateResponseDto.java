package com.example.app.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO-class
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
public class StateResponseDto {

    /**
     * Id of an entity in HA-server
     */
    private String entityId;

    /**
     * State of requested entity in HA
     */
    private String state;

}
