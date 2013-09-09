package ru.yaal.project.habrahabr.saver.article;

import org.testng.annotations.Test;
import ru.yaal.project.habrahabr.saver.url.UrlWrapper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

public class ArticleFactoryTest {
    @Test
    public void getHabrahabrArticle() throws Exception {
        UrlWrapper url = when(mock(UrlWrapper.class).getHost()).thenReturn("habrahabr.ru").getMock();
        assertTrue(ArticleFactory.getArticle(url) instanceof HabrahabrArticle);
    }

    @Test
    public void getCommonArticle() throws Exception {
        UrlWrapper url = when(mock(UrlWrapper.class).getHost()).thenReturn("oracle.com").getMock();
        assertTrue(ArticleFactory.getArticle(url) instanceof CommonArticle);
    }
}
