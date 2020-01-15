package io.tarrie.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.DbConstants;
import io.tarrie.model.constants.CharacterLimit;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.net.MalformedURLException;
import java.util.Collection;


// https://www.tutorialspoint.com/java/java_abstraction.htm

@DynamoDBTable(tableName = DbConstants.BASE_TABLE)
public class Entity {

    private String idCopy;
    private String id;
    private String name;
    private String imgPath;

    @DynamoDBAttribute(attributeName = DbAttributes.IMG_PATH)
    public String getImgPath() {
        return imgPath;
    }
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.SORT_KEY)
    public String getIdCopy() {
        return idCopy;
    }
    public void setIdCopy(String idCopy) {
        this.idCopy = idCopy;
    }

    @DynamoDBAttribute(attributeName = DbAttributes.HASH_KEY)
    @Size(min=1, max= CharacterLimit.SMALL)
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
