package io.tarrie.model.consumes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.model.constants.CharacterLimit;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.awt.*;

@ApiModel
public class EditGroup {
    @ApiModelProperty(value = "The ID of the user who is initiating the group creation")
    @NotNull
    public String adminUserId;

    @ApiModelProperty(value = "The name of the new group")
    @Size(min=1, max= CharacterLimit.SMALL)
    public String groupName;

    @ApiModelProperty(value = "The same thing as the groupID must be unique")
    @Size(min=1, max= CharacterLimit.SMALL)
    public String groupHandle;

    @Size(min=0, max=CharacterLimit.LARGE)
    @ApiModelProperty(value = "The biography of the new group")
    public String groupBio;

    @ApiModelProperty(value = "The profile pic of the group")
    public Image groupProfilePic;

    @Override
    public String toString() {
        return "CreateGroup [adminUserId=" + adminUserId + ", groupName=" + groupName + ", groupHandle="+ groupHandle+"]";
    }
}
