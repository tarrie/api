package io.tarrie.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.api.model.constants.EventVisibilityType;
import io.tarrie.api.model.constants.LinkVisibilityType;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ApiModel(description = "A sharable link to event")
public class SharableLink {
  // base62 ([A-Z, a-z, 0-9])
  @ApiModelProperty(
      notes =
          "hashkey of sharable link {eventId}#{randomString}, where {randomString} is a length 6 alphanumeric string",
          example = "xyz123#NLno15")
  @NotNull
  String hashKey;

  @NotNull
  LinkVisibilityType linkVisibilityType;

  @ApiModelProperty(notes = "Time that the hashtag was created (GMT)", hidden = true)
  @NotNull
  private LocalDateTime created;
}
