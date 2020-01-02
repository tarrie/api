package io.tarrie.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ApiModel(description="The time of a Tarrie Event")
public class EventTime {

    @ApiModelProperty(notes = "Time that the event was created (GMT)", required = true)
    @NotNull
    private LocalDateTime created;

    @ApiModelProperty(notes = "Time that the event starts (GMT)", required = true)
    private LocalDateTime start;

    @ApiModelProperty(notes = "Time that the event ends (GMT)", required = true)
    private LocalDateTime ends;




}
