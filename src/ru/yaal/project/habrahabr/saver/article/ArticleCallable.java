package ru.yaal.project.habrahabr.saver.article;

import org.apache.log4j.Logger;
import ru.yaal.project.habrahabr.saver.Resource;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;

import static java.lang.String.format;

/**
 * Загруажет статью и сохраняет ее на диск в отдельном потоке.
 * User: Aleks
 * Date: 11.09.13
 * Time: 9:21
 */
public class ArticleCallable implements Callable<IArticle> {
    private static final Logger LOG = Logger.getLogger(AutoCloseable.class);
    private IArticle article;
    private Path targetFolder;

    public ArticleCallable(IArticle article, Path targetFolder) {
        this.article = article;
        this.targetFolder = targetFolder;
    }

    @Override
    public IArticle call() throws Exception {
        LOG.debug(format("Запускаю поток для загрузки статьи %s", article));
        article.save(targetFolder);
        List<Resource> resources = article.getResources();
        for (Resource resource : resources) {
            resource.save(targetFolder);
        }
        return article;
    }
}
