package io.tarrie.utilities;

import io.tarrie.database.contants.DbAttributes;
import io.tarrie.database.contants.EntityTypeEnum;
import io.tarrie.database.contants.EventRelationshipEnum;
import io.tarrie.database.exceptions.MalformedInputException;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.*;

public class UtilityTest {
  @Test
  public void validateIdValidator() {
    assertTrue(Utility.isIdValid("EVT#-17904146509BmrGP", EntityTypeEnum.EVT));

    assertFalse(Utility.isIdValid("EVT#-17904146509BmrGP", EntityTypeEnum.GRP));
  }

  /**
   * Validates: Utility.eventIdToEventRelationship(), Utility.getEventRelationshipFromId(),
   * Utility.getEntityIdFromEventRelationshipPrefix()
   *
   * @throws MalformedInputException
   */
  @Test
  public void validateEventRelationshipStringManipulations() throws MalformedInputException {

    String eventID = "EVT#-17904146509BmrGP";
    String hostedEvent = Utility.eventIdToEventRelationship(eventID, EventRelationshipEnum.HOST);
    String rsvpEvent = Utility.eventIdToEventRelationship(eventID, EventRelationshipEnum.RSVP);
    String savedEvent = Utility.eventIdToEventRelationship(eventID, EventRelationshipEnum.SAVED);

    // Utility.eventIdToEventRelationship()
    assertEquals(String.format("%s#%s", EventRelationshipEnum.HOST, eventID), hostedEvent);

    // Utility.getEventRelationshipFromId()
    assertEquals(EventRelationshipEnum.HOST, Utility.getEventRelationshipFromId(hostedEvent));
    assertEquals(EventRelationshipEnum.RSVP, Utility.getEventRelationshipFromId(rsvpEvent));
    assertEquals(EventRelationshipEnum.SAVED, Utility.getEventRelationshipFromId(savedEvent));

    // Utility.getEntityIdFromEventRelationshipPrefix()
    assertEquals(eventID, Utility.getEntityIdFromEventRelationshipPrefix(hostedEvent));
    assertEquals(eventID, Utility.getEntityIdFromEventRelationshipPrefix(rsvpEvent));
    assertEquals(eventID, Utility.getEntityIdFromEventRelationshipPrefix(savedEvent));

  }
}
