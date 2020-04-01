package io.tarrie.api.model.produces;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@ApiModel
public class ViewGroupEvents {
    public GroupCondensed groupCondensed;

    @ApiModelProperty(notes = "The event that the group is hosting")
    @NotNull
    public Collection<EventCondensed> hostedEvents;

}
