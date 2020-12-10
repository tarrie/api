package io.tarrie.api.rest;

import io.tarrie.utilities.Utility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ApiStartUpTest {

  @BeforeAll
  static void setupClass() throws Exception {
    ApiStartUp.setSystemProperties();
  }

  @Test
  void TestSystemPropertiesLoad() {

    String nonce = Utility.getParam("nonce");
    Assertions.assertEquals("nonce", nonce);
  }
}
