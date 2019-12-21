package io.tarrie.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.api.model.constants.CharacterLimit;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

/**
 * ToDo: Add support for polls, documents associated with event
 */
@ApiModel(description="A Tarrie event")
public class Event {
    private static final int MIN = 2;
    private static final int MAX = 20;


    @ApiModelProperty(notes = "The unique identifier for the event")
    @NotNull
    String id;

    @ApiModelProperty(notes = "The name of the event")
    @Size(min = MIN, max = MAX)
    @NotNull
    String title;


    @ApiModelProperty(notes = "url on S3 that holds the events image")
    @NotNull
    String eventImgUrl;

    @ApiModelProperty(notes = "url on S3 that holds the events image")
    @NotNull
    Location location;

    @ApiModelProperty(notes = "the time of the event")
    @NotNull
    EventTime eventTime;

    @ApiModelProperty(notes = "the type of entity hosting the event", allowableValues = "User, Group")
    @NotNull
    HostType hostType;

    enum HostType{
        User, Group
    }

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

    @ApiModelProperty(value = "Collection of userIds invited to event")
    @NotNull
    public Collection<String> invitedUserIds;

    //FixMe: Should we add shared with. How to we want the business logic of sharing to be?

}
