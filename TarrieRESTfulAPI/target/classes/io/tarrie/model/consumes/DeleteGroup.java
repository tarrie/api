package io.tarrie.model.consumes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel
public class DeleteGroup {

    @ApiModelProperty(value = "The ID of the user who is initiating the group deletion")
    @NotNull
    public String userId;

    @Override
    public String toString() {
        return "DeleteGroup [userId=" + userId +"]";
    }
}


