package ru.yaal.project.habrahabr.saver;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
    private List<Article> articles;

    public FileParameters(File propertiesFile, File articlesFile) throws IOException {
        LOG.debug(format("Загружаю целевую папку из %s.", propertiesFile));
        LOG.debug(format("Загружаю URL статей из %s", articlesFile));
        targetFolder = loadTargerFolder(propertiesFile);
        articles = loadArticles(articlesFile);
    }

    private Path loadTargerFolder(File propertiesFile) throws IOException {
        Properties p = new Properties();
        p.load(new FileReader(propertiesFile));
        return Paths.get(p.getProperty(TARGET_FOLDER_PROPERTY_NAME));
    }

    private List<Article> loadArticles(File articlesFile) throws IOException {
        List<Article> result = new ArrayList<>();
        BufferedReader in = new BufferedReader(new FileReader(articlesFile));
        String line;
        while ((line = in.readLine()) != null) {
            if (!EMPTY_LINE_PATTERN.matcher(line).matches()) {
                try {
                    result.add(new Article(new URL(line)));
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
    public List<Article> getArticles() {
        return articles;
    }
}
