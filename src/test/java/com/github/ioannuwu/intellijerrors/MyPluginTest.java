package com.github.ioannuwu.intellijerrors;

import com.ioannuwu.inline.data.MyColorConverter;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

public class MyPluginTest {

    @Test
    public void colorConverterTest() {
        MyColorConverter colorConverter = new MyColorConverter();
        final Color mainColor = new Color(10, 15, 20);

        final String colorString = colorConverter.toString(mainColor);
        final Color fromString = colorConverter.fromString(colorString);

        Assert.assertEquals(mainColor, fromString);
    }
}
