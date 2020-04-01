package io.tarrie.model.produces;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.model.HashTag;
import io.tarrie.model.events.EventCondensed;
import io.tarrie.model.constants.ExploreType;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@ApiModel
public class TopLevelEvents {
    @ApiModelProperty(notes = "Exploration Type", allowableValues = "Home, Discover")
    @NotNull
    public ExploreType exploreType;

    @ApiModelProperty(notes = "Top Level Hashtags")
    @NotNull
    public Collection<HashTag> hashTags;

    @ApiModelProperty(notes = "Top Level Events")
    @NotNull
    public Collection<EventCondensed> events;
}
