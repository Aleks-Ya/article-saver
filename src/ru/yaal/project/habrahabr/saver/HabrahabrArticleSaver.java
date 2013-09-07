package ru.yaal.project.habrahabr.saver;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.URL;
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

    static {
        URL url = HabrahabrArticleSaver.class.getResource("log4j.xml");
        DOMConfigurator.configure(url);
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
     * @param args ������ ������ (��������, ����� ������� "http://habrahabr.ru/post/157165/", ����� ������ "157165").
     */
    public static void main(String[] args) throws IOException {
        try {
            Date start = new Date();
            LOG.info(format("������ ������: %s", SimpleDateFormat.
                    getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.MEDIUM).format(start)));
            LOG.info(format("��������� ����������: %s", Arrays.deepToString(args)));
            IParameters IParameters = new Parameters(args);
            Path targetFolder = IParameters.getTargetFolder();
            LOG.info(format("������� �����: %s", targetFolder.toString()));
            List<Article> articles = IParameters.getArticles();
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
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
