package io.tarrie.model.condensed;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.model.constants.CharacterLimit;
import io.tarrie.database.contants.DbAttributes;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel
public class EntityCondensed {
    private String id;
    private String userName;
    private String imgUrl;
    private String emailAddr;
    private String bio;

    @ApiModelProperty(notes = "The bio of the entity")
    @Size(min=0, max= CharacterLimit.MEDIUM)
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @ApiModelProperty(notes = "The unique identifier for the user", example = "beck_b_123")
    @NotNull
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @ApiModelProperty(notes = "username")
    @NotNull
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @ApiModelProperty(notes = "Email address of the user")
    @NotNull
    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.IMG_PATH)
    @ApiModelProperty(notes = "url that holds user profile pic")
    @NotNull
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
