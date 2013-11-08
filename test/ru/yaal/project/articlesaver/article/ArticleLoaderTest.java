package ru.yaal.project.articlesaver.article;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.yaal.project.articlesaver.DeleteDirVisitor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ArticleLoaderTest {
    static private int articleNameUniqueIndex;
    private Path path;

    private static List<IArticle> getArticleMocks(int size) throws IOException {
        List<IArticle> articles = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            IArticle article = mock(IArticle.class);
            when(article.getName()).thenReturn("Article" + articleNameUniqueIndex++);
            articles.add(article);
        }
        return articles;
    }

    @BeforeClass
    public void makePath() throws IOException {
        path = Files.createTempDirectory("article_saver_temp");
    }

    @Test
    public void loadOneArticle() throws Exception {
        ArticleLoader loader = new ArticleLoader(path);
        IArticle article = mock(IArticle.class);
        loader.load(article);
        Thread.sleep(200);
        verify(article).getResources();
        verify(article).getHtml();
    }

    @Test
    public void loadSomeArticles() throws Exception {
        ArticleLoader loader = new ArticleLoader(path);
        List<IArticle> articles = getArticleMocks(10);
        loader.load(articles);
        Thread.sleep(200);
        for (IArticle article : articles) {
            verify(article).getResources();
            verify(article).getHtml();
        }
    }

    @Test
    public void stop() throws Exception {
        ArticleLoader loader = new ArticleLoader(path);
        List<IArticle> articles = getArticleMocks(100);
        loader.load(articles);
        loader.stop();
        for (IArticle article : articles) {
            verify(article).getResources();
            verify(article).getHtml();
        }
    }

    @AfterClass
    public void cleanUp() throws IOException {
        Files.walkFileTree(path, new DeleteDirVisitor());
    }
}
