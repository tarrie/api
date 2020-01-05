package io.tarrie.database.datamodel;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import io.tarrie.Utility;
import io.tarrie.database.contants.DbConstants;
import io.tarrie.database.exceptions.MalformedInputException;

import javax.mail.internet.AddressException;
import java.net.MalformedURLException;
import java.net.URL;

@DynamoDBTable(tableName = DbConstants.BASE_TABLE)
public class NewUser {
    private String userId;
    private String userIdCopy;
    private static final String imgPath = DbConstants.DEFAULT_USER_IMG;
    private String name;
    private String emailAddr;

    @DynamoDBAttribute(attributeName = "data")
    public String getEmailAddr() {
        return emailAddr;
    }
    public void setEmailAddr(String email) throws AddressException {
        emailAddr= Utility.getEmailAddressFromString(email).toString();
    }

    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBHashKey(attributeName = "main_pk")
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) throws MalformedInputException {
        if (userId.matches("[A-Za-z0-9/._]+")){
            this.userId = "USR#"+userId;
        }else{
            throw new MalformedInputException("id is not alphanumeric: "+userId );
        }
    }

    @DynamoDBRangeKey(attributeName="main_sk")
    public String getUserIdCopy() {
        return userId;
    }
    public void setUserIdCopy(String userId) {
        this.userId = "USR#"+userId;
    }
}
