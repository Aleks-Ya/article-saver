package ru.yaal.project.habrahabr.saver.parameters;

import org.apache.log4j.Logger;
import ru.yaal.project.habrahabr.saver.url.UrlWrapper;
import ru.yaal.project.habrahabr.saver.article.ArticleFactory;
import ru.yaal.project.habrahabr.saver.article.IArticle;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import static java.lang.String.format;

/**
 * Обрабатывает параметры приложения из конфигурационных файлов.
 * User: Aleks
 * Date: 07.09.13
 * Time: 17:28
 */
public class FileParameters implements IParameters {
    public static final String DEFAULT_PROPERTIES_FILE_NAME = "HabrahabrArticleSaver.properties";
    public static final String DEFAULT_ARTICLES_FILE_NAME = "ArticleUrls.txt";
    public static final String TARGET_FOLDER_PROPERTY_NAME = "targetFolder";
    private static final Logger LOG = Logger.getLogger(FileParameters.class);
    private static final Pattern EMPTY_LINE_PATTERN = Pattern.compile("^\\s*$");
    private Path targetFolder;
    private List<IArticle> articles;

    public FileParameters(File propertiesFile, File articlesFile) throws IOException {
        this(new FileReader(propertiesFile), new FileReader(articlesFile));
        LOG.debug(format("Загружаю целевую папку из %s.", propertiesFile));
        LOG.debug(format("Загружаю URL статей из %s", articlesFile));
    }

    public FileParameters(Reader propertiesFile, Reader articlesFile) throws IOException {
        targetFolder = loadTargetFolder(propertiesFile);
        articles = loadArticles(articlesFile);
    }

    private Path loadTargetFolder(Reader propertiesFile) throws IOException {
        Properties p = new Properties();
        p.load(propertiesFile);
        return Paths.get(p.getProperty(TARGET_FOLDER_PROPERTY_NAME));
    }

    private List<IArticle> loadArticles(Reader articlesFile) throws IOException {
        List<IArticle> result = new ArrayList<>();
        BufferedReader in = new BufferedReader(articlesFile);
        String line;
        while ((line = in.readLine()) != null) {
            if (!EMPTY_LINE_PATTERN.matcher(line).matches()) {
                try {
                    result.add(ArticleFactory.getArticle(new UrlWrapper(line)));
                } catch (MalformedURLException e) {
                    LOG.warn(format("Пропускаю некорректную ссылку на статью: %s", line));
                }
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
}
