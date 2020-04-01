package io.tarrie.model.consumes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel
public class DeleteUserFromGroup {

    @ApiModelProperty(value = "The ID of the Admin (or Owner) that is removing user from group")
    @NotNull
    public String adminUserId;

    @ApiModelProperty(value = "The ID of user that is being removed")
    @NotNull
    public String memberUserId;
}
