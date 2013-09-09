package ru.yaal.project.habrahabr.saver;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;

/**
 * ������������ ��������� ���������� �� ��������� ������.
 * User: Aleks
 * Date: 03.09.13
 * Time: 6:51
 */
public class ConsoleParameters implements IParameters {
    private static final Logger LOG = Logger.getLogger(ConsoleParameters.class);
    private static final String URL_TEMPLATE = BASE_URL + "/post/%s/";
    private Path targetFolder;
    private List<Article> articles;

    public ConsoleParameters(String[] args) throws IOException {
        targetFolder = parseTargetFolder(args);
        articles = parseArticles(args);
        LOG.info("���������: " + toString());
    }

    private List<Article> parseArticles(String[] args) throws IOException {
        List<Article> result = new ArrayList<>(args.length);
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
            throw new IllegalArgumentException("�� ������� ��������� ����������: habrsaver d:/habr_articles 156395 183494");
        } else {
            result = Paths.get(args[0]);
            if (!result.toFile().exists()) {
                throw new IllegalArgumentException(format("������� ����� \"%s\" �� ����������.", result));
            }
            if (!result.toFile().isDirectory()) {
                throw new IllegalArgumentException(format("������� ����� \"%s\" �������� ������.", result));
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

    @Override
    public String toString() {
        return format("[��������� (������� �����=%s,���������� ������=%d]", targetFolder, articles.size());
    }
}
