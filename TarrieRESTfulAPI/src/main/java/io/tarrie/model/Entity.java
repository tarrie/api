package io.tarrie.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.DbConstants;
import io.tarrie.model.constants.CharacterLimit;

import javax.validation.constraints.Size;


// https://www.tutorialspoint.com/java/java_abstraction.htm
@DynamoDBTable(tableName = DbConstants.BASE_TABLE)
@DynamoDBDocument
public class Entity {

    private String idCopy;
    private String id;
    private String name;
    private String imgPath;

    @DynamoDBRangeKey(attributeName = DbAttributes.SORT_KEY)
    public String getIdCopy() {
        return idCopy;
    }
    public void setIdCopy(String idCopy) {
        this.idCopy = idCopy;
    }

    @DynamoDBHashKey(attributeName = DbAttributes.HASH_KEY)
    @Size(min=1, max= CharacterLimit.SMALL)
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.IMG_PATH)
    public String getImgPath() {
        return imgPath;
    }
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.NAME)
    @Size(min=1, max= CharacterLimit.SMALL)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


}
