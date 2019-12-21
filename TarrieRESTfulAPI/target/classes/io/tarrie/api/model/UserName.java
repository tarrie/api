package io.tarrie.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;

@ApiModel(description = "The name of a Tarrie user")
public class UserName {
    private static final int MIN = 2;
    private static final int MAX = 20;

    @Size(min = MIN, max = MAX)
    @ApiModelProperty(notes = "First name of User", example = "Becky", required = true)
    private String firstName= null;

    @Size(min = MIN, max = MAX)
    @ApiModelProperty(notes = "Last name of User", example = "Ruffini", required = true)
    private String lastName = null;

    /**
     * Sets the first and last name of the user
     *
     * @param firstName
     * @param lastName
     * @throws io.tarrie.api.model.exceptions.UserException
     */
    public void setName(String firstName, String lastName) throws io.tarrie.api.model.exceptions.UserException {
        if (firstName.length() <= MAX && firstName.length() >= MIN) {
            this.firstName = firstName;
        } else {
            throw new io.tarrie.api.model.exceptions.UserException(
                    "firstName is too short or too long:"
                            + "min char's is "
                            + MIN
                            + " max char's is "
                            + MAX);
        }

        if (lastName.length() <= MAX && lastName.length() >= MIN) {
            this.lastName = lastName;
        } else {
            throw new io.tarrie.api.model.exceptions.UserException(
                    "lastName is too short or too long:" + "min char's is " + MIN + " max char's is " + MAX);
        }
    }
}
