package io.tarrie.api.model.produces;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.api.model.Location;
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

    @ApiModelProperty(notes = "The state of the group ", example = "CA", position = 0)
    private String state;

    @ApiModelProperty(notes = "The city  of the group ", example = "Redlands", position = 1)
    private String city;

    @Size(min=0, max=30)
    @ApiModelProperty(notes="The name of the loc group is at", example="Northwestern University", position=3)
    private String locName;


}
