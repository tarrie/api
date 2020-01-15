package io.tarrie.model.constants;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Visibility Type of a event:<br />"+
        "- Private: Visible  to people and groups invited.<br />"+
        "- Public: Visible to people in the network")
public enum EventVisibilityType {
    Private, Public;
}
