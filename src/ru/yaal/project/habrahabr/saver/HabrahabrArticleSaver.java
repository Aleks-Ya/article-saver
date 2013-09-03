package ru.yaal.project.habrahabr.saver;

import org.apache.log4j.Logger;

import java.io.IOException;
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

    /**
     * @param args Номера постов (например, чтобы скачать "http://habrahabr.ru/post/157165/", нужно ввести "157165").
     */
    public static void main(String[] args) throws IOException {
        Date start = new Date();
        LOG.info(format("Начало работы: %s", SimpleDateFormat.
                getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.MEDIUM).format(start)));
        LOG.info(format("Параметры приложения: %s", Arrays.deepToString(args)));
        Parameters parameters = new Parameters(args);
        Path targetFolder = parameters.getTargetFolder();
        LOG.info(format("Целевая папка: %s", targetFolder.toString()));
        List<Article> articles = parameters.getArticles();
        for (Article article : articles) {
            article.save(targetFolder);
            List<Resource> resources = article.getResources();
            for (Resource resource : resources) {
                resource.load(targetFolder);
            }
        }
        Date finish = new Date();
        LOG.info(format("Конец работы: %s", SimpleDateFormat.
                getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.MEDIUM).format(finish)));
        LOG.info(format("Длительность работы: %d сек.", (finish.getTime() - start.getTime()) / 1000));
    }
}
