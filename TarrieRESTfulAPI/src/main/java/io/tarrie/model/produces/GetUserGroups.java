package io.tarrie.model.produces;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.model.condensed.GroupCondensed;

import java.util.Collection;

@ApiModel
public class GetUserGroups {
    @ApiModelProperty(notes = "groups the user is admin of ")
    public Collection<GroupCondensed> groupsAdminOf;

    @ApiModelProperty(notes = "groups the user is owner of ")
    public Collection<GroupCondensed> groupsOwnerOf;

    @ApiModelProperty(notes = "groups the user is member of ")
    public Collection<GroupCondensed> groupsMemberOf;

    @ApiModelProperty(notes = "groups the user is following ")
    public Collection<GroupCondensed> groupsFollowing;

}
