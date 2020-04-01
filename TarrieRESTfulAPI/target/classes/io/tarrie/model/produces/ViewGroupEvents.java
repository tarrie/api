package io.tarrie.model.produces;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.model.events.EventCondensed;
import io.tarrie.model.condensed.GroupCondensed;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@ApiModel
public class ViewGroupEvents {
    public GroupCondensed groupCondensed;

    @ApiModelProperty(notes = "The event that the group is hosting")
    @NotNull
    public Collection<EventCondensed> hostedEvents;

}
