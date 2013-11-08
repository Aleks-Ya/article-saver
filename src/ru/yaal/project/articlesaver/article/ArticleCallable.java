package ru.yaal.project.articlesaver.article;

import org.apache.log4j.Logger;
import ru.yaal.project.articlesaver.writer.IArticleWriter;
import ru.yaal.project.articlesaver.writer.PathArticleWriter;

import java.nio.file.Path;
import java.util.concurrent.Callable;

import static java.lang.String.format;

/**
 * Загруажет статью и сохраняет ее на диск в отдельном потоке.
 * User: Aleks
 * Date: 11.09.13
 * Time: 9:21
 */
class ArticleCallable implements Callable<IArticle> {
    private static final Logger LOG = Logger.getLogger(AutoCloseable.class);
    private final IArticle article;
    private final IArticleWriter articleWriter;

    public ArticleCallable(IArticle article, Path targetFolder) {
        this.article = article;
        articleWriter = new PathArticleWriter(targetFolder);
    }

    @Override
    public IArticle call() throws Exception {
        LOG.debug(format("Запускаю поток для загрузки статьи %s", article));
        articleWriter.save(article);
        return article;
    }
}
