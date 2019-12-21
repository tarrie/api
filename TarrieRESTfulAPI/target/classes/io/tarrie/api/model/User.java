package io.tarrie.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.properties.EmailProperty;

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

    private static final int MIN = 2;
    private static final int MAX = 20;

    @ApiModelProperty(notes = "The name of the user", example = "Becky")
    @NotNull
    private UserName userName;

    @ApiModelProperty(notes = "The email address of the user")
    @NotNull
    public InternetAddress emailAddr;

    @ApiModelProperty(notes = "Array of group memberships")
    @NotNull
    private ArrayList<Membership> memberships = new ArrayList<>();

    /**
     * Add email address
     * @param email
     * @throws AddressException
     */
    void addEmail(String email) throws AddressException {
        emailAddr = new InternetAddress(email);
        emailAddr.validate();
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
