package io.tarrie.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.database.exceptions.MalformedInputException;
import io.tarrie.model.constants.EventVisibilityType;

import javax.validation.constraints.NotNull;

@DynamoDBDocument
@ApiModel(description = "Private specifications for a event")
public class EventPrivacy {

    // Private or Public
    private String visibilityType;
    private boolean isInvitable;

    @ApiModelProperty(notes = "Boolean indicating if guest can invite their friends ")
    @NotNull
    public boolean getInvitable() {
        return isInvitable;
    }
    public void setInvitable(boolean invitable) {
        isInvitable = invitable;
    }

    @ApiModelProperty(notes = "VisibilityType of the event")
    @NotNull
    public String getVisibilityType() {
        return visibilityType;
    }

    /**
     * Sets the visibility type of event
     * @param visibilityType name of enum {@link EventVisibilityType}
     * @throws MalformedInputException if visibilityType is not in {@link EventVisibilityType} enum
     */
    public void setVisibilityType(String visibilityType) throws MalformedInputException {
        try{
            EventVisibilityType.valueOf(visibilityType);
        } catch (IllegalArgumentException e){
            throw new MalformedInputException("Malformed visibility type: "+visibilityType);
        }
        this.visibilityType = visibilityType;
    }

}
