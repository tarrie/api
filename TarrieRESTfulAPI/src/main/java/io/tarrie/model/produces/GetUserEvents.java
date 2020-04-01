package io.tarrie.model.produces;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.model.events.EventCondensed;

import java.util.Collection;

@ApiModel
public class GetUserEvents {

    @ApiModelProperty(notes = "events the user is hosting")
    public Collection<EventCondensed> hostedEvents;

    @ApiModelProperty(notes = "events the user has saved")
    public Collection<EventCondensed> savedEvents;

    @ApiModelProperty(notes = "events the user is going to")
    public Collection<EventCondensed> rspvEvents;


}
