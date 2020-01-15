package io.tarrie.model.condensed;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.DbConstants;
import io.tarrie.model.Location;
import io.tarrie.model.ProfileImg;
import io.tarrie.model.constants.CharacterLimit;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@DynamoDBTable(tableName = DbConstants.BASE_TABLE)
@ApiModel
public class GroupCondensed {
    private String groupId;
    private String groupIdCopy;
    private  String imgPath = DbConstants.DEFAULT_GROUP_IMG;
    private String name;
    private String bio;
    private Location location;
    private UserCondensed owner;
    private Set<UserCondensed> admins;

    // Hash key - primary key
    @DynamoDBHashKey(attributeName = DbAttributes.HASH_KEY)
    @ApiModelProperty(notes = "The unique identifier for the group")
    @NotNull
    @Size(min=1, max= CharacterLimit.SMALL)
    public String getGroupId() {
        return groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    // range key
    @DynamoDBRangeKey(attributeName=DbAttributes.SORT_KEY)
    @NotNull
    public String getGroupIdCopy() {
        return groupIdCopy;
    }
    public void setGroupIdCopy(String groupIdCopy) {
        this.groupIdCopy = groupIdCopy;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.NAME)
    @Size(min=1, max= CharacterLimit.SMALL)
    @ApiModelProperty(notes = "The name of the group", example = "NU Chess")
    @NotNull
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.IMG_PATH)
    @ApiModelProperty(notes = "url that holds user profile pic")
    @NotNull
    public String getImgPath() {
        return imgPath;
    }
    public void setImgPath(String imgPath){
        this.imgPath = imgPath;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.BIO)
    @Size(min=0, max= CharacterLimit.MEDIUM)
    @ApiModelProperty(value = "The biography of the group")
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.LOC)
    @ApiModelProperty(notes = "The location of the group")
    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.OWNER)
    @ApiModelProperty(notes = "The owner of the group")
    @NotNull
    public UserCondensed getOwner() {
        return owner;
    }
    public void setOwner(UserCondensed owner) {
        this.owner = owner;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.ADMINS)
    @ApiModelProperty(notes = "The set of admins of the group")
    public Set<UserCondensed> getAdmins() {
        return admins;
    }
    public void setAdmins(Set<UserCondensed> admins) {
        this.admins = admins;
    }
}
