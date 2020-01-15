package io.tarrie.model.consumes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.model.EventPrivacy;
import io.tarrie.model.EventTime;
import io.tarrie.model.HashTag;
import io.tarrie.model.Location;
import io.tarrie.model.constants.CharacterLimit;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.awt.*;
import java.util.Collection;

@ApiModel
public class EditEvent {
    @ApiModelProperty(value = "The id of user editing the event")
    @NotNull
    public String userId;

    @ApiModelProperty(notes = "The privacy specifications of the event")
    EventPrivacy eventPrivacy;

    @ApiModelProperty(value = "The time of the event")
    public EventTime eventTime;

    @ApiModelProperty(value = "The event location (if left empty the event is virtual")
    public Location eventLocation;

    @ApiModelProperty(value = "The profile pic of the event")
    public Image eventProfilePic;

    @ApiModelProperty(value = "Collection of entityIds invited to event")
    @NotNull
    public Collection<String> invitedEntityIds;

    @ApiModelProperty(value = "The description of the event")
    @Size(min=0, max= CharacterLimit.LARGE)
    public String eventDescription;

    @ApiModelProperty(value = "The title of the event")
    @Size(min=1, max= CharacterLimit.SMALL)
    public String eventTitle;

    @ApiModelProperty(notes = "hash tags associated with event")
    public Collection<HashTag> hashTags;

    @ApiModelProperty(value = "Boolean saying if link sharing is on or not(default is off)")
    public boolean isLinkSharingOn;
}
