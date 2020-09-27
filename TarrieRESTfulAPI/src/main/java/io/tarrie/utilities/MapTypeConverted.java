package io.tarrie.utilities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;


//https://stackoverflow.com/questions/51300211/dynamodbmappingexception-on-mapping-mapstring-objectattribute

/**
 * So that dynamo knows how to convert map
 */
public class MapTypeConverted implements DynamoDBTypeConverter<String, Map> {
    @Override
    public String convert(Map s) {
        try {
            return new ObjectMapper().writeValueAsString(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map unconvert(String s) {
        try {
            return new ObjectMapper().readValue(s, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
