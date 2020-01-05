package io.tarrie.api.model.constants;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "{Home: events of groups that user is following or is a member of, Discover:  events across the network}")
public enum ExploreType {
    Home, Discover
}
