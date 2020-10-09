package io.tarrie.model.consumes;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.DbConstants;
import io.tarrie.utilities.Utility;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@DynamoDBTable(tableName = DbConstants.BASE_TABLE)
@ApiModel(description = "Entity Identifier")
@DynamoDBDocument
public class EntityId {
  private String primaryKey = null;
  private String sortKey;

  @ApiModelProperty(value = "The hash key of the entity (primary key)")
  @NotNull
  @JsonProperty(DbAttributes.HASH_KEY)
  @DynamoDBHashKey(attributeName = DbAttributes.HASH_KEY)
  public String getPrimaryKey() {
    return primaryKey;
  }

  @ApiModelProperty(value = "The range key of the entity (sort/secondary key)")
  @JsonProperty(DbAttributes.SORT_KEY)
  @DynamoDBRangeKey(attributeName = DbAttributes.SORT_KEY)
  public String getSortKey() {
    return sortKey;
  }

  public void setPrimaryKey(String primaryKey) {
    this.primaryKey = primaryKey;
  }

  public void setSortKey(String sortKey) {
    this.sortKey = sortKey;
  }


  @Override
  public String toString() {

    return String.format(
        "EntityId(primary_key=%s%s)",
        primaryKey, sortKey != null ? String.format(", sort_key=%s)", sortKey) : "");
  }
}
