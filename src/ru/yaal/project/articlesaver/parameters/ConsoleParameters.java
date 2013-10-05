package ru.yaal.project.articlesaver.parameters;

import org.apache.log4j.Logger;
import ru.yaal.project.articlesaver.article.ArticleFactory;
import ru.yaal.project.articlesaver.article.IArticle;
import ru.yaal.project.articlesaver.url.UrlWrapper;

import java.io.File;
import java.io.IOException;
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
    private final File targetFolder;
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
            result.add(ArticleFactory.getArticle(url));
        }
        return result;
    }

    private File parseTargetFolder(String[] args) {
        File result;
        if (args == null || args.length < 2) {
            throw new IllegalArgumentException("Не указаны параметры приложения: habrsaver d:/habr_articles 156395 183494");
        } else {
            result = FilePathParser.parse(args[0]);
        }
        return result;
    }

    @Override
    public File getTargetFolder() {
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