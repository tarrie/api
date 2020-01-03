package io.tarrie.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.api.model.constants.CharacterLimit;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

@ApiModel(description="A Tarrie group organization")
public class Group extends Entity{

    public Group(String id){
        super(id);
    }

    @ApiModelProperty(notes = "The members of the group")
    @NotNull
    Collection<Membership> members;

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
