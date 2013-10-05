package ru.yaal.project.articlesaver;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ru.yaal.project.articlesaver.article.ArticleLoader;
import ru.yaal.project.articlesaver.article.IArticle;
import ru.yaal.project.articlesaver.parameters.ConsoleParameters;
import ru.yaal.project.articlesaver.parameters.FileParameters;
import ru.yaal.project.articlesaver.parameters.IParameters;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;

/**
 * Запускает закачку статей с habrahabr.ru
 * User: Aleks
 * Date: 30.08.13
 * Time: 6:43
 */
public class ArticleSaver {
    private static final Logger LOG = Logger.getLogger(ArticleSaver.class);

    static {
        DOMConfigurator.configure(ArticleSaver.class.getResource("log4j.xml"));
        String consoleEncoding = System.getProperty("consoleEncoding");
        if (consoleEncoding != null) {
            try {
                System.setOut(new PrintStream(System.out, true, consoleEncoding));
                ConsoleAppender consoleAppender = (ConsoleAppender) Logger.getRootLogger().getAppender("consoleAppender");
                consoleAppender.setWriter(new OutputStreamWriter(System.out));
            } catch (java.io.UnsupportedEncodingException ex) {
                System.err.println("Unsupported encoding set for console: " + consoleEncoding);
            }
        }
    }

    /**
     * @param args Номера постов (например, чтобы скачать "http://habrahabr.ru/post/157165/", нужно ввести "157165").
     */
    public static void main(String[] args) {
        try {
            long start = System.currentTimeMillis();
            LOG.debug(format("Начало работы: %s", SimpleDateFormat.
                    getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.MEDIUM).format(start)));
            LOG.debug(format("Параметры приложения: %s", Arrays.deepToString(args)));
            IParameters parameters = parseParameters(args);
            Path targetFolder = parameters.getTargetFolder();
            createTargetFolder(targetFolder);
            LOG.info(format("Целевая папка: %s", targetFolder.toString()));
            List<IArticle> articles = parameters.getArticles();
            LOG.info(format("Статей для загрузки: %d", articles.size()));
            ArticleLoader loader = new ArticleLoader(targetFolder);
            if (articles.size() > 0) {
                loader.load(articles);
                loader.stop();
                long finish = System.currentTimeMillis();
                LOG.debug(format("Конец работы: %s", SimpleDateFormat.
                        getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.MEDIUM).format(finish)));
                LOG.info(format("Длительность работы: %d сек.", (finish - start) / 1000));
            } else {
                LOG.info("Перехожу в режим слушателя буфера обмена");
                new ClipboardListener(loader);
            }
            LOG.debug("Выход из приложения");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * Если папка не существует, создает ее (на всю глубину).
     */
    private static void createTargetFolder(Path targetFolder) {
        File target = targetFolder.toFile();
        if (!target.exists()) {
            boolean created = target.mkdirs();
            LOG.debug(format("Создана целевая папка (результат %b): %s", created, target));
        } else {
            LOG.debug(format("Создана целевая папка уже существует: %s", target));
        }
    }

    private static IParameters parseParameters(String[] args) throws IOException {
        IParameters parameters;
        if (args.length != 0) {
            parameters = new ConsoleParameters(args);
        } else {
            String currentDir = System.getProperty("user.dir") + "\\";
            File propertiesFile = new File(currentDir + FileParameters.DEFAULT_PROPERTIES_FILE_NAME);
            File articlesFile = new File(currentDir + FileParameters.DEFAULT_ARTICLES_FILE_NAME);
            parameters = new FileParameters(propertiesFile, articlesFile);
        }
        return parameters;
    }
}
