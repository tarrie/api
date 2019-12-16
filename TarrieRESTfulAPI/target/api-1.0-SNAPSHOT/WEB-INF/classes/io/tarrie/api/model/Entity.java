package io.tarrie.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;


// https://www.tutorialspoint.com/java/java_abstraction.htm

@ApiModel(description="A Tarrie Entity. Either a User or a Group")
public abstract class Entity {

    @ApiModelProperty(notes = "The unique identifier for the entity", example = "xr563",  required = true, position = 0)
    private String id;

    @ApiModelProperty(notes = "url on S3 that holds the entities profile picture")
    @NotNull
    private ProfileImg profileImg;

    @ApiModelProperty(notes = "The events that the entity is hosting")
    @NotNull
    private ArrayList<Event> hostedEvents = new ArrayList<>();

    // The constructor.
    public Entity(String id){
        this.id = id;
    }

    /**
     * Adds a events to the list of events
     * @param event
     */
    void addEvent(Event event){
        hostedEvents.add(event);
    }

    /**
     * Adds a collection of events to the entities list of events
     * @param events
     */
    void addEvent(Collection<Event> events){
        hostedEvents.addAll(events);
    }

    /**
     * Gets the list of events for the entity.
     * @return
     */
    public ArrayList<Event> getHostedEvents() {
        return hostedEvents;
    }

    /**
     * Sets the profile img url for the entitiy
     * @param profileImgUrl
     */
    public void setProfileImgUrl(String profileImgUrl) throws MalformedURLException {
        this.profileImg = new ProfileImg(profileImgUrl);
    }

    /**
     * Gets the profile img url of the entity
     * @return
     */
    public String getProfileImgUrl() {
        return profileImg.toString();
    }

    /**
     * Gets the identifier for the entitiy
     * @return
     */
    public String getId() {
        return id;
    }
}
