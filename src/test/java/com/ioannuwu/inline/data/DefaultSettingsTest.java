package com.ioannuwu.inline.data;

import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

public class DefaultSettingsTest {

    @Test
    public void defaultSettingsTest() {
        assertNotNull("", DefaultSettings.ERROR);
        assertNotNull("", DefaultSettings.WARNING);
        assertNotNull("", DefaultSettings.WEAK_WARNING);
        assertNotNull("", DefaultSettings.IGNORE_LIST);
        assertNotNull("", DefaultSettings.FONT);
    }
}
