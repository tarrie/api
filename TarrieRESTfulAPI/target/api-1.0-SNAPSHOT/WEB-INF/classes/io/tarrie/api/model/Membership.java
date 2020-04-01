package io.tarrie.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.api.model.constants.CharacterLimit;
import io.tarrie.api.model.constants.MembershipType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(description="Indicates the membership type a `user' has with a Group")
public class Membership {

    @ApiModelProperty(notes = "The identifier for user associated to group",example = "xr563",  required = true, position = 1)
    public String userId;

    @ApiModelProperty(notes = "The name of the  user")
    @NotNull
    @Size(min=1, max= CharacterLimit.SMALL)
    public String userName;

    @ApiModelProperty(notes = "The profile img of the user")
    public ProfileImg userProfileImg;

    @ApiModelProperty(notes = "Membership Type of User",allowableValues = "Owner, Admin, Member, Follower",example = "Subscriber",  required = true, position = 0)
    public MembershipType memType;



    @ApiModelProperty(notes = "identifier the group that user is associated to",  required = true)
    public String groupId;

    @ApiModelProperty(notes = "The profile img of the group")
    public ProfileImg groupProfileImg;


}
