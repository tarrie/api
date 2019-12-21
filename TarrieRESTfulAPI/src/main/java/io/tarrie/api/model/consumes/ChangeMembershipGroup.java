package io.tarrie.api.model.consumes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.api.model.constants.MembershipType;

@ApiModel
public class ChangeMembershipGroup {

  @ApiModelProperty(notes = "UserId of the user that is initiating the membership change")
  public String adminUserId;


  @ApiModelProperty(
      notes = "Membership Type of User to change to",
      allowableValues = "Owner, Admin, Subscriber",
      example = "Subscriber",
      required = true)
  public MembershipType newMemType;

  @Override
  public String toString() {
    return "ChangeMembershipGroup [adminUserId="
        + adminUserId
        + ", newMemType="
        + newMemType
        + "]";
  }
}
