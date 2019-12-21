package io.tarrie.api.model.produces;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.api.model.ProfileImg;
import io.tarrie.api.model.UserName;

import javax.mail.internet.InternetAddress;
import javax.validation.constraints.NotNull;

@ApiModel
public class UserCondensed {
    @ApiModelProperty(notes = "The unique identifier for the user", example = "beck_b_123",  position = 0)
    @NotNull
    public String id;

    @NotNull
    UserName userName;

    @NotNull
    public ProfileImg profileImg;

    @ApiModelProperty(notes = "Email address of the user")
    @NotNull
    public InternetAddress emailAddr;
}
