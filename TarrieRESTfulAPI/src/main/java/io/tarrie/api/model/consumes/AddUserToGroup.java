package io.tarrie.api.model.consumes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@ApiModel
public class AddUserToGroup {

    @ApiModelProperty(value = "The ID of the user who is initiating the `AddUserToGroup' request")
    @NotNull
    public String userId;

    @ApiModelProperty(value = "The ID of the group that the new people will be in")
    @NotNull
    public String groupId;

    @ApiModelProperty(value = "The IDs of the Users will be in")
    public Collection<String> newUserIds;

    @Override
    public String toString() {
        return "AddUserToGroup [userId=" + userId + ", groupId=" + groupId + ", newUserIds="+ newUserIds.toString()+"]";
    }
}
