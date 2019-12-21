package io.tarrie.api.model.consumes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.api.model.constants.MembershipType;

@ApiModel
public class ChangeMembershipGroup {
  @ApiModelProperty(notes = "groupId of the group")
  public String groupId;

  @ApiModelProperty(notes = "UserId of the user that is initiating the membership change")
  public String adminUserId;

  @ApiModelProperty(notes = "groupId of the group member who is getting membership changed")
  public String memberUserId;

  @ApiModelProperty(
      notes = "Membership Type of User to change to",
      allowableValues = "Owner, Admin, Subscriber",
      example = "Subscriber",
      required = true)
  public MembershipType newMemType;

  @Override
  public String toString() {
    return "ChangeMembershipGroup [groupId="
        + groupId
        + ", adminUserId="
        + adminUserId
        + ", memberUserId="
        + memberUserId
        + ", newMemType="
        + newMemType
        + "]";
  }
}
