package ru.yaal.project.articlesaver.parameters;

import org.apache.log4j.Logger;
import ru.yaal.project.articlesaver.article.ArticleFactory;
import ru.yaal.project.articlesaver.article.IArticle;
import ru.yaal.project.articlesaver.url.UrlWrapper;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.lang.String.format;

/**
 * Обрабатывает параметры приложения из конфигурационных файлов.
 * User: Aleks
 * Date: 07.09.13
 * Time: 17:28
 */
public class FileParameters implements IParameters {
    public static final String DEFAULT_PROPERTIES_FILE_NAME = "ArticleSaver.properties";
    public static final String DEFAULT_ARTICLES_FILE_NAME = "ArticleUrls.txt";
    public static final String TARGET_FOLDER_PROPERTY_NAME = "targetFolder";
    private static final Logger LOG = Logger.getLogger(FileParameters.class);
    private static final Pattern EMPTY_LINE_PATTERN = Pattern.compile("^\\s*$");
    private File targetFolder;
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

    private File loadTargetFolder(Reader propertiesFile) throws IOException {
        StringBuilder line = new StringBuilder();
        try (BufferedReader br = new BufferedReader(propertiesFile)) {
            int first;
            while ((first = br.read()) != -1) {
                if (first != '\\') {
                    line.append((char) first);
                } else {
                    line.append("\\");
                    int second = br.read();
                    if (second != -1) {
                        line.append((char) second);
                    }
                }
            }
        }
        String path = line.toString().split("targetFolder=")[1];
        return FilePathParser.parse(path);
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
    public File getTargetFolder() {
        return targetFolder;
    }

    @Override
    public List<IArticle> getArticles() {
        return articles;
    }
}
