package io.tarrie.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.api.model.constants.EventVisibilityType;

import javax.validation.constraints.NotNull;

@ApiModel(description = "Private specifications for a event")
public class EventPrivacy {

    // Private or Public
    public EventVisibilityType visibilityType;

    @ApiModelProperty(notes = "Boolean indicating if guest can invite their friends ")
    @NotNull
    public boolean isInvitable;


}
