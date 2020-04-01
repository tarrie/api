package io.tarrie.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.Utility;
import io.tarrie.database.exceptions.MalformedInputException;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@DynamoDBDocument
@ApiModel(description="The time of a Tarrie Event")
public class EventTime {


    private String createdTime;
    private String startTime;
    private String endTime;

    @ApiModelProperty(notes = "Time that the event was created (GMT)", required = true)
    @NotNull
    public String getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(String created) throws MalformedInputException {
        Utility.isDateTimeValid(created);
        this.createdTime = created;
    }

    @ApiModelProperty(notes = "Time that the event starts (GMT)", required = true)
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String strDateTime) throws MalformedInputException {
        Utility.isDateTimeValid(strDateTime);
        this.endTime = strDateTime;
    }

    @ApiModelProperty(notes = "Time that the event was created (GMT)", required = true)
    @NotNull
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) throws MalformedInputException {
        Utility.isDateTimeValid(startTime);
        this.startTime = startTime;
    }
}
