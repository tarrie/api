package io.tarrie.api.model.consumes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.api.model.EventTime;
import io.tarrie.api.model.HashTag;
import io.tarrie.api.model.Location;
import io.tarrie.api.model.User;
import io.tarrie.api.model.constants.CharacterLimit;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.awt.*;
import java.util.Collection;

@ApiModel
public class CreateEvent {

    @ApiModelProperty(value = "The time of the event")
    @NotNull
    public EventTime eventTime;

    @ApiModelProperty(value = "The event location (if left empty the event is virtual")
    public Location eventLocation;

    @ApiModelProperty(value = "The id of user creating event")
    @NotNull
    public String userId;

    @ApiModelProperty(value = "If left empty then the user is creating a event under own account, and not a group account")
    @NotNull
    public String groupId;

    @ApiModelProperty(value = "The profile pic of the event")
    public Image eventProfilePic;

    @ApiModelProperty(value = "Collection of userIds invited to event")
    @NotNull
    public Collection<String> invitedUserIds;

    @ApiModelProperty(value = "The description of the event")
    @Size(min=0, max= CharacterLimit.LARGE)
    public String eventDescription;

    @ApiModelProperty(value = "The title of the event")
    @Size(min=1, max= CharacterLimit.SMALL)
    @NotNull
    public String eventTitle;

    @ApiModelProperty(notes = "hash tags associated with event")
    Collection<HashTag> hashTags;
}
