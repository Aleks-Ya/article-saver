package ru.yaal.project.habrahabr.saver.parameters;

import org.apache.log4j.Logger;
import ru.yaal.project.habrahabr.saver.UrlWrapper;
import ru.yaal.project.habrahabr.saver.article.Article;
import ru.yaal.project.habrahabr.saver.article.IArticle;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;

/**
 * Обрабатывает параметры приложения из командной строки.
 * User: Aleks
 * Date: 03.09.13
 * Time: 6:51
 */
public final class ConsoleParameters implements IParameters {
    private static final Logger LOG = Logger.getLogger(ConsoleParameters.class);
    private static final String URL_TEMPLATE = BASE_URL + "/post/%s/";
    private final Path targetFolder;
    private List<IArticle> articles;

    public ConsoleParameters(String[] args) throws IOException {
        targetFolder = parseTargetFolder(args);
        articles = parseArticles(args);
        LOG.info("Параметры: " + toString());
    }

    private List<IArticle> parseArticles(String[] args) throws IOException {
        List<IArticle> result = new ArrayList<>(args.length);
        String[] postIds = Arrays.copyOfRange(args, 1, args.length);
        for (String postId : postIds) {
            UrlWrapper url = new UrlWrapper(format(URL_TEMPLATE, postId));
            result.add(new Article(url));
        }
        return result;
    }

    private Path parseTargetFolder(String[] args) {
        Path result;
        if (args == null || args.length < 2) {
            throw new IllegalArgumentException("Не указаны параметры приложения: habrsaver d:/habr_articles 156395 183494");
        } else {
            result = Paths.get(args[0]);
            if (!result.toFile().exists()) {
                throw new IllegalArgumentException(format("Целевая папка \"%s\" не существует.", result));
            }
            if (!result.toFile().isDirectory()) {
                throw new IllegalArgumentException(format("Целевая папка \"%s\" является файлом.", result));
            }
        }
        return result;
    }

    @Override
    public Path getTargetFolder() {
        return targetFolder;
    }

    @Override
    public List<IArticle> getArticles() {
        return articles;
    }

    @Override
    public String toString() {
        return format("[Параметры (целевая папка=%s,количество статей=%d]", targetFolder, articles.size());
    }
}