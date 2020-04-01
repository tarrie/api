package io.tarrie.api.model.consumes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.api.model.constants.ExploreType;

import javax.validation.constraints.NotNull;

@ApiModel
public class GetTopLevelEvents {
    @ApiModelProperty(notes = "Exploration Type", allowableValues = "Home, Discover")
    @NotNull
    public ExploreType exploreType;
}
