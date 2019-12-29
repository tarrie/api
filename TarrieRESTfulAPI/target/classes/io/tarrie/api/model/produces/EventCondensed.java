package io.tarrie.api.model.produces;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.api.model.*;
import io.tarrie.api.model.constants.CharacterLimit;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@ApiModel
public class EventCondensed extends Event {
    @ApiModelProperty(notes = "The unique identifier for the event")
    @NotNull
    public String id;

    @ApiModelProperty(notes = "The name of the event")
    @Size(min = 1, max = CharacterLimit.SMALL)
    @NotNull
    public String title;

    @ApiModelProperty(notes = "url on S3 that holds the events image")
    @NotNull
    public String eventImgUrl;

    @ApiModelProperty(notes = "location of event")
    public Location location;

    @ApiModelProperty(notes = "time of the event")
    @NotNull
    public EventTime eventTime;

    @ApiModelProperty(notes = "the number of rsvps to the event")
    @NotNull
    int rsvpNum;

    @ApiModelProperty(notes = "The unique identifier for creator of event (groupId or userId)")
     @NotNull
    public String creatorId;

    @ApiModelProperty(notes = "The name of the creator of the event")
    @NotNull
    public String creatorName;

    @ApiModelProperty(notes = "url on S3 that holds the creator profile picture")
    @NotNull
    public ProfileImg creatorProfileImg;



}
