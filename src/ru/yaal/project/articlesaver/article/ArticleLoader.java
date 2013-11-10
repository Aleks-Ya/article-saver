package ru.yaal.project.articlesaver.article;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static java.lang.String.format;

/**
 * Загружает статьи в несколько потоков.
 * User: Aleks
 * Date: 21.09.13
 * Time: 12:26
 */
public class ArticleLoader implements ILoadingInfo {
    private static final Logger LOG = Logger.getLogger(ArticleLoader.class);
    private static final int THREAD_POOL_SIZE = 30;
    private final ThreadPoolExecutor es = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private final Path targetFolder;

    public ArticleLoader(Path targetFolder) {
        this.targetFolder = targetFolder;
    }

    public void load(IArticle article) throws IOException {
        LOG.info(format("Запланировал загрузку: %s", article.getUrl()));
        es.submit(new ArticleCallable(article, targetFolder));
    }

    public void load(List<IArticle> articles) throws IOException {
        for (IArticle article : articles) {
            load(article);
        }
    }

    public void stop() throws InterruptedException {
        es.shutdown();
        while (loadingNow() > 0) {
            Thread.sleep(500);
        }
    }

    @Override
    public int waitForLoading() {
        return (int) es.getTaskCount();
    }

    @Override
    public int loadingNow() {
        return es.getActiveCount();
    }

    @Override
    public int hasLoaded() {
        return (int) es.getCompletedTaskCount();
    }
}
