package io.tarrie.api.interfaces;

import javax.mail.internet.InternetAddress;

public interface Search {
  /** Searches for a group by groupName- ElasticSearch returns : {id, name, imgURL, emailaddress} */
  public void searchForEntity(String groupName);

  /**
   * Searches for a user by firstName, or last name or both- ElasticSearch returns : {id, name,
   * imgURL, emailaddress}
   */
  public void searchForEntity(String firstName, String lastName);

  /**
   * Searches for a user email address - ElasticSearch returns : {id, name, imgURL, emailaddress} //
   * https://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
   */
  public void searchForEntity(InternetAddress emailAddress);


}
