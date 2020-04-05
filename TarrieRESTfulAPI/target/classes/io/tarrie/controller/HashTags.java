package io.tarrie.controller;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import io.tarrie.database.TarrieDynamoDb;
import io.tarrie.database.exceptions.MalformedInputException;
import io.tarrie.model.HashTag;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class HashTags {



    static void inputHashTag(Set<String> hashTags, String eventId) throws MalformedInputException{
        String datetime = Instant.now().toString();


        // convert hashtags to the HashTag objects
        Set<HashTag> hashTagSet= hashTags.stream()
                .map(String::valueOf)
                .map((tag)->{
                            HashTag hashTag = new HashTag();
                            hashTag.setEventId(eventId);
                            hashTag.setHashTag(tag);
                            hashTag.setTimeCreated(datetime);
                            return hashTag;
                }).collect(Collectors.toSet());

        // upload the batch data to dynomoDb
        TarrieDynamoDb.batchWriteOutcome(hashTagSet);
    }
}
