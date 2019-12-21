package io.tarrie.api;

import io.tarrie.api.model.Entity;
import io.tarrie.api.model.EventTime;
import io.tarrie.api.model.Location;
import io.tarrie.api.model.ProfileImg;

import java.util.Collection;

/**
 * Create event
 *      - {ProfileImg, EventTime, Location}
 *      - Promote Event:
 */
public class Events {

    /**
     * Create event
     */
    void createEvent(ProfileImg profileImg, EventTime eventTime, Location location){}

    /**
     * Share a event with User or Group
     */
    void eventShare(Entity tarrieEntity, Collection<Entity> entitiesToShareWith){}


}
