package io.tarrie.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;


@ApiModel
public class HashTag {
    @ApiModelProperty(value="hash tag value")
    @NotNull
    public String tagValue;

    @ApiModelProperty(value="related tags")
    Collection<HashTag> relatedTags;

    @ApiModelProperty(notes = "Time that the hashtag was created (GMT)", hidden = true)
    @NotNull
    private LocalDateTime created;
}
