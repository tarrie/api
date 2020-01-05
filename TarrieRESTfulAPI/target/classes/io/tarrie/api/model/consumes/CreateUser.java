package io.tarrie.api.model.consumes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.api.model.ProfileImg;
import io.tarrie.api.model.constants.CharacterLimit;

import javax.mail.internet.InternetAddress;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel
public class CreateUser {
    @ApiModelProperty(notes = "The unique identifier for the entity", example = "xr563",  position = 0)
    @Size(min=1, max= CharacterLimit.SMALL)
    @NotNull
    public String id;

    @ApiModelProperty(notes = "The name of the entity", example = "Becky")
    @NotNull
    @Size(min=1, max= CharacterLimit.SMALL)
    public String name;


    @ApiModelProperty(notes = "The email address of the user")
    @NotNull
    public String emailAddr;

}
