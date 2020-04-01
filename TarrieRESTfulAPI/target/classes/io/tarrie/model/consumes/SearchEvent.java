package io.tarrie.model.consumes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.model.Location;

import javax.validation.constraints.NotNull;

@ApiModel
public class SearchEvent {

    @ApiModelProperty(value = "The location of the event")
    public Location location;

    @ApiModelProperty(value = "The id of the user making search request")
    @NotNull
    public String userId;
}
