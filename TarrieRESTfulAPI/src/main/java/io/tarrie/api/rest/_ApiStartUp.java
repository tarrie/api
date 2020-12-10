package io.tarrie.api.rest;


import com.sun.jersey.api.model.AbstractResourceModelContext;
import com.sun.jersey.api.model.AbstractResourceModelListener;
import io.tarrie.utilities.Utility;

import javax.ws.rs.ext.Provider;
import java.util.Properties;

@Provider
public class ApiStartUp implements AbstractResourceModelListener
{
    // Name of the prop file that contains env variables for graphQl
    private static final String graphqlPropFileName = "src/main/resources/graphql.properties";
    private static final String awsPropFileName = "src/main/resources/aws.properties";

    @Override
    public void onLoaded(AbstractResourceModelContext modelContext) {

        setSystemProperties();
        System.out.println("##### System properties initiated");
    }

    /**
     * Sets System Properties to be accessed globally
     * @see  <a href="http://tutorials.jenkov.com/java-collections/properties.html#system-properties"> properties tutorial</>
     */
     static void setSystemProperties(){
        Properties systemProperties= System.getProperties();

        // Load appsync property
        Properties appSyncProperties = Utility.loadPropertyValues(graphqlPropFileName);
        systemProperties.setProperty("AppSyncApiUrl", appSyncProperties.getProperty("ApiUrl"));
        systemProperties.setProperty("AppSyncApiKey", appSyncProperties.getProperty("ApiKey"));

        // Load aws property
        Properties awsProperties = Utility.loadPropertyValues(awsPropFileName);
        systemProperties.setProperty("AWS_ACCESS_KEY_ID", awsProperties.getProperty("AWS_ACCESS_KEY_ID"));
        systemProperties.setProperty("AWS_SECRET_ACCESS_KEY", awsProperties.getProperty("AWS_SECRET_ACCESS_KEY"));
        systemProperties.setProperty("nonce", appSyncProperties.getProperty("nonce"));


    }
}
