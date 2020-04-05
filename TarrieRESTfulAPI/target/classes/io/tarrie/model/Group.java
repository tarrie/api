package io.tarrie.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.sun.codemodel.internal.util.UnicodeEscapeWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.DbConstants;
import io.tarrie.model.condensed.UserCondensed;
import io.tarrie.model.constants.CharacterLimit;
import io.tarrie.model.events.Event;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@DynamoDBTable(tableName = DbConstants.BASE_TABLE)
@ApiModel(description="A Tarrie group organization")
@DynamoDBDocument
public class Group {

    private String idCopy;
    private String id;
    private String name;
    private String imgPath;
    private Location loc;
    private String bio;
    private String owner;
    private Set<String> admins;

    private List<Event> hostedEvents; // has to be queried
    private List<Entity> contacts; // has to be queried
    private List<UserCondensed> members;  // has to be queried

    /* ********* getters ***********/
    @DynamoDBAttribute(attributeName = DbAttributes.NAME)
    @ApiModelProperty(notes = "The name of the group")
    @NotNull
    @Size(min=1, max= CharacterLimit.SMALL)
    public String getName() {
        return name;
    }

    @DynamoDBHashKey(attributeName = DbAttributes.HASH_KEY)
    @ApiModelProperty(notes = "The unique identifier for the group", example = "GRP#xr563",  required = true, position = 0)
    @Size(min=1, max= CharacterLimit.SMALL)
    @NotNull
    public String getId() {
        return id;
    }

    @DynamoDBRangeKey(attributeName = DbAttributes.HASH_KEY)
    public String getIdCopy() {
        return idCopy;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.IMG_PATH)
    @ApiModelProperty(notes = "url on S3 that holds the groups profile picture")
    @NotNull
    public String getImgPath() {
        return imgPath;
    }

    // needs to be queried
    @ApiModelProperty(notes = "The contacts of the group")
    @DynamoDBIgnore
    public List<Entity> getContacts() {
        return contacts;
    }


    // needs to be queried
    @ApiModelProperty(notes = "The events that the group is hosting")
    @DynamoDBIgnore
    public List<Event> getHostedEvents() {
        return hostedEvents;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.LOC)
    @ApiModelProperty(notes = "The location of the group")
    public Location getLoc() {
        return loc;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.BIO)
    @ApiModelProperty(notes = "The bio of the group")
    @Size(min=0, max= CharacterLimit.MEDIUM)
    public String getBio() {
        return bio;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.OWNER)
    @ApiModelProperty(notes = "The userId of the owner of the group")
    public String getOwner() {
        return owner;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.ADMINS)
    @ApiModelProperty(notes = "The set of userId's of the admins of the group")
    public Set<String> getAdmins() {
        return admins;
    }

    @ApiModelProperty(notes = "The members of the group")
    @NotNull
    public List<UserCondensed> getMembers() {
        return members;
    }

    /* ********* setters ***********/
    public void setName(String name) {
        this.name = name;
    }
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
    public void setContacts(List<Entity> contacts) {
        this.contacts = contacts;
    }
    public void setHostedEvents(List<Event> hostedEvents) {
        this.hostedEvents = hostedEvents;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setLoc(Location loc) { this.loc = loc; }
    public void setIdCopy(String idCopy) { this.idCopy = idCopy; }
    public void setBio(String bio) { this.bio = bio; }
    public void setOwner(String owner) { this.owner = owner; }
    public void setAdmins(Set<String> admins) { this.admins = admins; }
    public void setMembers(List<UserCondensed> members) { this.members = members; }
}
