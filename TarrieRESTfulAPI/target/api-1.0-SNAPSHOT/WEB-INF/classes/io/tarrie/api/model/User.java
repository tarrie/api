package io.tarrie.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.properties.EmailProperty;
import io.tarrie.Utility;
import io.tarrie.api.model.produces.EventCondensed;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@ApiModel(description = "A Tarrie user")
public class User extends Entity {

    public User(String id){
        super(id);
    }

    @ApiModelProperty(notes = "The email address of the user")
    @NotNull
    public InternetAddress emailAddr;

    @ApiModelProperty(notes = "Array of group memberships")
    @NotNull
    private Collection<Membership> memberships;

    @ApiModelProperty(notes = "events the user has saved")
    public Collection<EventCondensed> savedEvents;

    @ApiModelProperty(notes = "events the user is going to")
    public Collection<EventCondensed> rsvpEvents;

    /**
     * Add email address
     * @throws AddressException if email is malformed
     */
    void setEmailAddr(String email) throws AddressException {
        emailAddr= Utility.getEmailAddressFromString(email);
    }

    /**
     * Adds a membership to the user list of memberships
     * @param membership
     */
    void addMembership(Membership membership){
        memberships.add(membership);
    }

    /**
     * Adds a collection of membership's to the user's list of memberships
     * @param memberships
     */
    void addMembership(Collection<Membership> memberships){
        this.memberships.addAll(memberships);
    }




}
