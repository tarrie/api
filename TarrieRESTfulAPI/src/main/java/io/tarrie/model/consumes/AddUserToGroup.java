package io.tarrie.model.consumes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@ApiModel
public class AddUserToGroup {

    @ApiModelProperty(value = "The ID of the user who is initiating the `AddUserToGroup' request")
    @NotNull
    public String adminUserId;


    @ApiModelProperty(value = "The IDs of the users to add")
    public Collection<String> newUserIds;

    @Override
    public String toString() {
        return "AddUserToGroup [adminUserId=" + adminUserId  + ", newUserIds="+ newUserIds.toString()+"]";
    }
}
