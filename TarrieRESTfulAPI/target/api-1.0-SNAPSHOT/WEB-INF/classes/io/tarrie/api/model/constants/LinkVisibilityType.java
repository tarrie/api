package io.tarrie.api.model.constants;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Visibility Type of a event link:<br />"+
        "- Public: Anyone who has link can access. No sign-in required")
public enum LinkVisibilityType {
    Public
}

