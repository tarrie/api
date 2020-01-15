package io.tarrie.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.net.URL;

@ApiModel(description = "A Tarrie profile picture")
public class ProfileImg {

  @ApiModelProperty(notes = "url on S3 that holds the profile pic")
  @NotNull
  URL imgUrl;

  ProfileImg(String spec) throws MalformedURLException {
    imgUrl = new URL(spec);
  }
}
