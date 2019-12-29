package io.tarrie.api.model.consumes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel
public class EventId {
    @ApiModelProperty(value = "The id of the event")
    @NotNull
    public String userId;
}
