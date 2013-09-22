package ru.yaal.project.articlesaver.article;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.String.format;

/**
 * Загружает статьи в несколько потоков.
 * User: Aleks
 * Date: 21.09.13
 * Time: 12:26
 */
public class ArticleLoader {
    private static final Logger LOG = Logger.getLogger(ArticleLoader.class);
    private static final int THREAD_POOL_SIZE = 30;
    private final List<Future<IArticle>> futures = new ArrayList<>();
    private final ExecutorService es = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private final Path targetFolder;

    public ArticleLoader(Path targetFolder) {
        this.targetFolder = targetFolder;
    }

    public void load(IArticle article) throws InterruptedException, IOException {
        LOG.info(format("Запланировал загрузку: %s", article.getUrl()));
        futures.add(es.submit(new ArticleCallable(article, targetFolder)));
    }

    public void load(List<IArticle> articles) throws InterruptedException, IOException {
        for (IArticle article : articles) {
            load(article);
        }
    }

    public void stop() throws InterruptedException {
        es.shutdown();
        while (futures.size() > 0) {
            for (Future<IArticle> future : new ArrayList<>(futures)) {
                if (future.isDone()) {
                    futures.remove(future);
                }
            }
            Thread.sleep(500);
        }
    }

}
