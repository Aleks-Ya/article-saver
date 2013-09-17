package ru.yaal.project.articlesaver.article;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.yaal.project.articlesaver.url.UrlWrapper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

public class ArticleFactoryTest {
    @Test
    public void getHabrahabrArticle() throws Exception {
        UrlWrapper url = when(mock(UrlWrapper.class).getHost()).thenReturn("habrahabr.ru").getMock();
        assertTrue(ArticleFactory.getArticle(url) instanceof HabrahabrArticle);
    }

    @Test(dataProvider = "wikipediaUrls")
    public void getWikipediaArticle(String urlStr) throws Exception {
        UrlWrapper url = when(mock(UrlWrapper.class).getHost()).thenReturn(urlStr).getMock();
        assertTrue(ArticleFactory.getArticle(url) instanceof WikipediaArticle);
    }

    @Test(dataProvider = "hhUrls")
    public void getHeadHunterArticle(String urlStr) throws Exception {
        UrlWrapper url = when(mock(UrlWrapper.class).getHost()).thenReturn(urlStr).getMock();
        assertTrue(ArticleFactory.getArticle(url) instanceof HeadHunterArticle);
    }

    @DataProvider
    public Object[][] wikipediaUrls() {
        return new Object[][]{
                {"wikipedia.org"},
                {"ru.wikipedia.org"},
                {"en.wikipedia.org"}
        };
    }

    @DataProvider
    public Object[][] hhUrls() {
        return new Object[][]{
                {"hh.ru"},
                {"spb.hh.ru"}
        };
    }

    @Test
    public void getCommonArticle() throws Exception {
        UrlWrapper url = when(mock(UrlWrapper.class).getHost()).thenReturn("oracle.com").getMock();
        assertTrue(ArticleFactory.getArticle(url) instanceof CommonArticle);
    }
}
