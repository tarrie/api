package io.tarrie.utilities;

import io.tarrie.database.contants.EntityTypeEnum;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UtilityTest {
    @Test
    public void validateIdValidator(){
        assertTrue(Utility.isIdValid("EVT#-17904146509BmrGP", EntityTypeEnum.EVT));
        assertFalse(Utility.isIdValid("EVT#-17904146509BmrGP", EntityTypeEnum.GRP));
    }

}
