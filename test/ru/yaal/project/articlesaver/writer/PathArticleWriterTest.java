package ru.yaal.project.articlesaver.writer;

import org.testng.annotations.Test;
import ru.yaal.project.articlesaver.article.IArticle;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.mock;

public class PathArticleWriterTest {

    @Test(expectedExceptions = ArticleWriteException.class,
            expectedExceptionsMessageRegExp = "Target folder don't exists: .*")
    public void targetFolderNotExists() throws Exception {
        Path targetFolder = Paths.get("not_exists");
        IArticleWriter writer = new PathArticleWriter(targetFolder);
        writer.save(mock(IArticle.class));
    }
}
