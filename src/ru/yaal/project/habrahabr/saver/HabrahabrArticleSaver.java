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
 * ��������� ������� ������ � habrahabr.ru
 * User: Aleks
 * Date: 30.08.13
 * Time: 6:43
 */
public class HabrahabrArticleSaver {
    private static final Logger LOG = Logger.getLogger(HabrahabrArticleSaver.class);

    /**
     * @param args ������ ������ (��������, ����� ������� "http://habrahabr.ru/post/157165/", ����� ������ "157165").
     */
    public static void main(String[] args) throws IOException {
        Date start = new Date();
        LOG.info(format("������ ������: %s", SimpleDateFormat.
                getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.MEDIUM).format(start)));
        LOG.info(format("��������� ����������: %s", Arrays.deepToString(args)));
        Parameters parameters = new Parameters(args);
        Path targetFolder = parameters.getTargetFolder();
        LOG.info(format("������� �����: %s", targetFolder.toString()));
        List<Article> articles = parameters.getArticles();
        for (Article article : articles) {
            article.save(targetFolder);
            List<Resource> resources = article.getResources();
            for (Resource resource : resources) {
                resource.load(targetFolder);
            }
        }
        Date finish = new Date();
        LOG.info(format("����� ������: %s", SimpleDateFormat.
                getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.MEDIUM).format(finish)));
        LOG.info(format("������������ ������: %d ���.", (finish.getTime() - start.getTime()) / 1000));
    }
}
