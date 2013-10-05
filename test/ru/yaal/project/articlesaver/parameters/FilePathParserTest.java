package ru.yaal.project.articlesaver.parameters;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class FilePathParserTest {
    @Test(dataProvider = "provider")
    public void testParse(String actPath, String expPath) throws Exception {
        assertEquals(FilePathParser.parse(actPath).getAbsolutePath(), expPath);
    }

    @DataProvider
    public Object[][] provider() {
        final String standard = "c:\\temp\\articles";
        return new Object[][]{
                {"c:\temp\\articles", standard},
                {"c:\temp\\articles\\", standard},
                {"c:\review\tortoise\forever\never\u0000", "c:\\review\\tortoise\\forever\\never"},
                {"c:\\temp\\articles", standard},
                {"c:\\temp\\articles\\", standard},
                {"c:\\\\temp\\\\articles", standard},
                {"c:\\\\temp\\\\articles\\\\", standard},
                {"c:/temp/articles", standard},
                {"c:/temp/articles/", standard},
                {"c://temp//articles", standard},
                {"c://temp//articles//", standard},
        };
    }
}
