package com.increff.pos.util;

import com.increff.pos.AbstractUnitTest;
import org.junit.Test;

import static com.increff.pos.util.StringUtil.isEmpty;
import static junit.framework.TestCase.assertEquals;

public class StringUtilTest extends AbstractUnitTest {

    @Test
    public void testIsEmpty(){
        String emptyString = "";
        String nullString = null;
        String nonEmptyString = "not empty";

        assertEquals(true, isEmpty(emptyString));
        assertEquals(true, isEmpty(nullString));
        assertEquals(false, isEmpty(nonEmptyString));
    }
}
