package io.tarrie.api;

import javax.mail.internet.InternetAddress;

/**
 * Incorporating static signals in score - > https://www.elastic.co/guide/en/elasticsearch/reference/current/static-scoring-signals.html
 */
public class Search {

    /**
     * Searches for a group by groupName- ElasticSearch
     * returns : {id, name, imgURL, emailaddress}
     */
    public void searchForEntity(String groupName){}
    /**
     * Searches for a user by firstName, or last name or both- ElasticSearch
     * returns : {id, name, imgURL, emailaddress}
     */
    public void searchForEntity(String firstName, String lastName){}
    /**
     * Searches for a user email address - ElasticSearch
     * returns : {id, name, imgURL, emailaddress}
     */
    public void searchForEntity(InternetAddress emailAddress){
        // https://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
    }



}
