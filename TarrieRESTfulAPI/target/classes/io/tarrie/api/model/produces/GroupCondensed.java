package io.tarrie.api.model.produces;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.api.model.ProfileImg;
import io.tarrie.api.model.constants.CharacterLimit;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel
public class GroupCondensed {
    @ApiModelProperty(notes = "The unique identifier for the group")
    @NotNull
    public String id;

    @Size(min=1, max= CharacterLimit.SMALL)
    @ApiModelProperty(notes = "The name of the group", example = "NU Chess")
    @NotNull
    String name;

    @NotNull
    public ProfileImg profileImg;

    @Size(min=0, max= CharacterLimit.LARGE)
    @ApiModelProperty(value = "The biography of the group")
    public String bio;


}
