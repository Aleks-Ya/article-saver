package ru.yaal.project.articlesaver.article;

import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ArticleLoaderTest {
    File path = mock(File.class);

    private static List<IArticle> getArticleMocks(int size) {
        List<IArticle> articles = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            articles.add(mock(IArticle.class));
        }
        return articles;
    }

    @Test
    public void loadOneArticle() throws Exception {
        ArticleLoader loader = new ArticleLoader(path);
        IArticle article = mock(IArticle.class);
        loader.load(article);
        Thread.sleep(200);
        verify(article, times(1)).getResources();
        verify(article, times(1)).save(path);
    }

    @Test
    public void loadSomeArticles() throws Exception {
        ArticleLoader loader = new ArticleLoader(path);
        List<IArticle> articles = getArticleMocks(10);
        loader.load(articles);
        Thread.sleep(200);
        for (IArticle article : articles) {
            verify(article, times(1)).getResources();
            verify(article, times(1)).save(path);
        }
    }

    @Test
    public void stop() throws Exception {
        ArticleLoader loader = new ArticleLoader(path);
        List<IArticle> articles = getArticleMocks(100);
        loader.load(articles);
        loader.stop();
        for (IArticle article : articles) {
            verify(article, times(1)).getResources();
            verify(article, times(1)).save(path);
        }
    }
}
