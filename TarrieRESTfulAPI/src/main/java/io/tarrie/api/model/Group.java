package io.tarrie.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;
import java.util.ArrayList;

@ApiModel(description="A Tarrie group organization")
public class Group extends Entity{

    public Group(String id){
        super(id);
    }

    @Size(min=0, max=30)
    @ApiModelProperty(notes = "The name of the group", example = "NU Chess", required = true, position = 1)
     String name;

    @ApiModelProperty(notes = "The members of the group", example = "NU Chess", required = true, position = 1)
     ArrayList<Membership> members = new ArrayList<>();


}
