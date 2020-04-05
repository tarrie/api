package io.tarrie.model.consumes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel
public class UserId {


    private String userId;

    @ApiModelProperty(value = "The id of the user")
    @NotNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
