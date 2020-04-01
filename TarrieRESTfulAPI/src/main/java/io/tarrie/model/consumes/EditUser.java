package io.tarrie.model.consumes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.model.constants.CharacterLimit;

import javax.mail.internet.InternetAddress;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.awt.Image;

@ApiModel
public class  EditUser {
    @ApiModelProperty(value = "The ID of the user who is initiating the user edit")
    @NotNull
    public String userId;

    @ApiModelProperty(value = "The new name of the user")
    @Size(min=1, max= CharacterLimit.SMALL)
    public String newUserName;

    @ApiModelProperty(value = "The new user userId ")
    @Size(min=1, max= CharacterLimit.SMALL)
    public String newUserId;

    @ApiModelProperty(value = "The new profile pic of the user")
    public Image newProfilePic;

    @ApiModelProperty(notes = "The new email address of the user")
    public InternetAddress newEmailAddr;

}
