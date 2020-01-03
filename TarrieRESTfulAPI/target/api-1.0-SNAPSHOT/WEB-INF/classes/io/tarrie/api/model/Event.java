package io.tarrie.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.api.model.constants.CharacterLimit;
import io.tarrie.api.model.constants.EventLimits;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

/**
 * ToDo: Add support for polls, documents associated with event
 */
@ApiModel(description="A Tarrie event")
public class Event {


    /**
     * Link sharing is ON - When click shrarablelink
     */
    @ApiModelProperty(notes = "The unique identifier for the event")
    @NotNull
    String eventId;

    @ApiModelProperty(notes = "The privacy specifications of the events")
    @NotNull
    EventPrivacy privacy;

    @ApiModelProperty(notes = "The name of the event")
    @Size(min = 1, max = CharacterLimit.SMALL)
    @NotNull
    String title;


    @ApiModelProperty(notes = "url on S3 that holds the events image")
    @NotNull
            @Size(min=1, max = EventLimits.MAX_NUM_OF_IMGS)
    String eventImgUrl;

    @ApiModelProperty(notes = "the location of event")
    @NotNull
    Location location;

    @ApiModelProperty(notes = "the time of the event")
    @NotNull
    EventTime eventTime;

    @ApiModelProperty(notes = "the entity hosting the event. Either a User or a Group")
    @NotNull
    Entity host;

    @ApiModelProperty(notes = "the user's that have rsvp'd (going to) to the event")
    @NotNull
    Collection<User> rsvps;

    @ApiModelProperty(notes = "hash tags associated with event")
    Collection<HashTag> hashTags;

    @ApiModelProperty(value = "The description of the event")
    @Size(min=0, max= CharacterLimit.LARGE)
    public String eventDescription;

    @ApiModelProperty(value = "Collection of entityIds invited to event")
    @NotNull
    public Collection<String> invitedEntityIds;

    @ApiModelProperty(value = "Boolean saying if link sharing is on or not(default is off)")
    public boolean isLinkSharingOn;

    @ApiModelProperty(value = "Collection of SharableLinks associated to the event",hidden = true)
    @NotNull
    public Collection<SharableLink> sharableLinks;

    //FixMe: Should we add shared with. How to we want the business logic of sharing to be?

}
