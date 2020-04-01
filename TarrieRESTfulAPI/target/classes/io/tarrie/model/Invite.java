package io.tarrie.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.DbConstants;

@DynamoDBTable(tableName = DbConstants.BASE_TABLE)
public class Invite {
    private String entityId;
    private String invitedId;

    @DynamoDBHashKey(attributeName = DbAttributes.HASH_KEY)
    public String getEntityId() {
        return entityId;
    }

    @DynamoDBRangeKey(attributeName = DbAttributes.SORT_KEY)
    public String getInvitedId() {
        return invitedId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public void setInvitedId(String invitedId) {
        this.invitedId = invitedId;
    }
}
