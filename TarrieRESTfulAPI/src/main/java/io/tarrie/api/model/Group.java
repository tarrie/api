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

}
