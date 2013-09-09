package ru.yaal.project.habrahabr.saver;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.lang.String.format;

/**
 * Запускает закачку статей с habrahabr.ru
 * User: Aleks
 * Date: 30.08.13
 * Time: 6:43
 */
public class HabrahabrArticleSaver {
    private static final Logger LOG = Logger.getLogger(HabrahabrArticleSaver.class);

    static {
        DOMConfigurator.configure(HabrahabrArticleSaver.class.getResource("log4j.xml"));
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
    public static void main(String[] args) throws IOException {
        try {
            Date start = new Date();
            LOG.debug(format("Начало работы: %s", SimpleDateFormat.
                    getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.MEDIUM).format(start)));
            LOG.debug(format("Параметры приложения: %s", Arrays.deepToString(args)));
            IParameters parameters;
            if (args.length != 0) {
                parameters = new ConsoleParameters(args);
            } else {
                String currentDir = System.getProperty("user.dir") + "\\";
                File propertiesFile = new File(currentDir + FileParameters.DEFAULT_PROPERTIES_FILE_NAME);
                File articlesFile = new File(currentDir + FileParameters.DEFAULT_ARTICLES_FILE_NAME);
                parameters = new FileParameters(propertiesFile, articlesFile);
            }
            Path targetFolder = parameters.getTargetFolder();
            LOG.info(format("Целевая папка: %s", targetFolder.toString()));
            List<Article> articles = parameters.getArticles();
            LOG.info(format("Статей для загрузки: %d", articles.size()));
            for (Article article : articles) {
                article.save(targetFolder);
                List<Resource> resources = article.getResources();
                for (Resource resource : resources) {
                    resource.load(targetFolder);
                }
            }
            Date finish = new Date();
            LOG.debug(format("Конец работы: %s", SimpleDateFormat.
                    getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.MEDIUM).format(finish)));
            LOG.info(format("Длительность работы: %d сек.", (finish.getTime() - start.getTime()) / 1000));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
