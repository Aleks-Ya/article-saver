package ru.yaal.project.habrahabr.saver;

import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

import static org.testng.Assert.assertEquals;

public class FileParametersTest {
    @Test
    public void testGetTargetFolder() throws IOException {
        File propertiesFile = File.createTempFile("properties_habrahabr", ".tmp");
        propertiesFile.deleteOnExit();
        String expTargerFolder = "c:\\temp\\out\\folder";
        Properties p = new Properties();
        p.setProperty(FileParameters.TARGET_FOLDER_PROPERTY_NAME, expTargerFolder);
        p.store(new FileOutputStream(propertiesFile), "commmmmmment");

        File articlesFile = File.createTempFile("articles_habrahabr", ".tmp");
        propertiesFile.deleteOnExit();
        String expArticle1 = "http://habrahabr.ru/post/192320/";
        String expArticle2 = "http://habrahabr.ru/company/JetBrains/blog/184202/";
        try (PrintWriter out = new PrintWriter(articlesFile)) {
            out.println(expArticle1);
            out.println(expArticle2);
        }

        IParameters parameters = new FileParameters(propertiesFile, articlesFile);
        assertEquals(parameters.getTargetFolder().toString(), expTargerFolder);
        assertEquals(parameters.getArticles(),
                Arrays.asList(new Article(new URL(expArticle1)), new Article(new URL(expArticle2))));
    }
}
