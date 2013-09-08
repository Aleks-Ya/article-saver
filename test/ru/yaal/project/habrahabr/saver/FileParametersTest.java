package ru.yaal.project.habrahabr.saver;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class FileParametersTest {
    private Reader targetFolderReader;

    @BeforeMethod
    public void beforeMethod() {
        targetFolderReader = new StringReader(FileParameters.TARGET_FOLDER_PROPERTY_NAME + "=c:/temp/out/folder");
    }

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

    @Test
    public void emptyUrl() throws IOException {
        Reader articles = new StringReader(" \n\\t\n \\t");
        IParameters parameters = new FileParameters(targetFolderReader, articles);
        assertTrue(parameters.getArticles().isEmpty());
    }

    @Test
    public void incorrectUrl() throws IOException {
        Reader articles = new StringReader("j39jl324jsdh");
        IParameters parameters = new FileParameters(targetFolderReader, articles);
        assertTrue(parameters.getArticles().isEmpty());
    }
}
