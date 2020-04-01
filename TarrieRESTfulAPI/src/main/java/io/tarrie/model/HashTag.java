package io.tarrie.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.DbConstants;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;


@DynamoDBTable(tableName = DbConstants.BASE_TABLE)
@ApiModel
public class HashTag {
    private String eventId;
    private String hashTag;
    private String timeCreated;
    private Set<String> relatedTags;

    @DynamoDBHashKey(attributeName = DbAttributes.HASH_KEY)
    @ApiModelProperty(notes = "eventId attached to hastag")
    @NotNull
    public String getEventId() {
        return eventId;
    }

    @DynamoDBRangeKey(attributeName = DbAttributes.SORT_KEY)
    @ApiModelProperty(value="hash tag value")
    @NotNull
    public String getHashTag() {
        return hashTag;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.RELATED_TAGS)
    @ApiModelProperty(value="related tags")
    public Set<String> getRelatedTags() {
        return relatedTags;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.DATA)
    @ApiModelProperty(notes = "Time that the hashtag was created (GMT)", hidden = true)
    @NotNull
    public String getTimeCreated() {
        return timeCreated;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public void setRelatedTags(Set<String> relatedTags) {
        this.relatedTags = relatedTags;
    }



}
