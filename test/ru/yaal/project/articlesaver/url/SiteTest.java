package ru.yaal.project.articlesaver.url;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static ru.yaal.project.articlesaver.url.Site.*;

public class SiteTest {
    @Test
    public void getHabrahabrArticle() throws Exception {
        assertEquals(Site.resolve("http://habrahabr.ru/post/194670/"), HABRAHABR);
    }

    @Test(dataProvider = "wikipediaUrls")
    public void getWikipediaArticle(String urlStr) throws Exception {
        assertEquals(Site.resolve(urlStr), WIKIPEDIA);
    }

    @Test(dataProvider = "hhUrls")
    public void getHeadHunterArticle(String urlStr) throws Exception {
        assertEquals(Site.resolve(urlStr), HEAD_HUNTER);
    }

    @DataProvider
    public Object[][] wikipediaUrls() {
        return new Object[][]{
                {"http://www.wikipedia.org/"},
                {"http://ru.wikipedia.org/wiki/%D0%91%D1%83%D1%84%D0%B5%D1%80_%D0%BE%D0%B1%D0%BC%D0%B5%D0%BD%D0%B0"},
                {"http://en.wikipedia.org/wiki/Klipper"}
        };
    }

    @DataProvider
    public Object[][] hhUrls() {
        return new Object[][]{
                {"http://hh.ru/vacancy/7377874"},
                {"http://spb.hh.ru/vacancy/6607858"}
        };
    }

    @Test
    public void getCommonArticle() throws Exception {
        assertEquals(Site.resolve("http://stackoverflow.com/questions/18944923"), UNKNOWN);
    }
}
