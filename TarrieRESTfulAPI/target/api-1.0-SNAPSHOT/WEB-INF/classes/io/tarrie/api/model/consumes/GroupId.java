package io.tarrie.api.model.consumes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel
public class GroupId {
    @ApiModelProperty(value = "The id of the grpu[")
    @NotNull
    public String userId;
}
