package io.tarrie.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;

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
    String name;

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
    ArrayList<User> rsvps = new ArrayList<>();

    //FixMe: Should we add shared with. How to we want the business logic of sharing to be?

}
