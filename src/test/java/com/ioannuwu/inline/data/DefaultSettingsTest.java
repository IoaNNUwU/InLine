package com.ioannuwu.inline.data;

import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

public class DefaultSettingsTest {

    @Test
    public void defaultSettingsTest() {
        assertNotNull("There is no ERROR in default settings", DefaultSettings.ERROR);
        assertNotNull("There is no WARNING in default settings", DefaultSettings.WARNING);
        assertNotNull("There is no WEAK WARNING in default settings", DefaultSettings.WEAK_WARNING);
        assertNotNull("There is no IGNORE LIST in default settings", DefaultSettings.IGNORE_LIST);
        assertNotNull("There is no FONT in default settings", DefaultSettings.FONT);
    }
}
