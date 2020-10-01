package io.tarrie.database;

import io.tarrie.utilities.Utility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TarrieAppSyncTest {

  @Test
  public void GraphQlPropertiesLoad() {

    String nonce = TarrieAppSync.getProperties().getProperty("nonce");
    Assertions.assertEquals("nonce", nonce);
  }
}
