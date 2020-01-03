package io.tarrie.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarrie.api.model.constants.CharacterLimit;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(description="The location of a Tarrie entity. Its OK if this object is empty because the event could be virtual")
public class Location {

    @ApiModelProperty(notes = "The state of the location ", example = "CA")
    @NotNull
    public String state;

    @ApiModelProperty(notes = "The city  location ", example = "Redlands")
    @NotNull
    public String city;

    @ApiModelProperty(notes = "The zipcode of location ", example = "92373")
    @NotNull
    public int zipCode;

    @ApiModelProperty(notes = "Line 1 of a standard street address form", example = "1623 W Columbia Ave")
    @NotNull
    public String line1;

    @ApiModelProperty(notes = "Line 2 of a standard street address form", example = "APT 2")
    public String line2;

    @Size(min=0, max= CharacterLimit.SMALL)
    @ApiModelProperty(notes="The location name", example="Bogo Party House")
    public String locName;

    @ApiModelProperty(notes="The latitude of the location", example="34.055560")
    public float latitude;

    @ApiModelProperty(notes="The longitude of the location", example="-117.182600", position=5)
    public float longitude;

}
