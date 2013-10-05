package ru.yaal.project.articlesaver.article;

import ru.yaal.project.articlesaver.Resource;
import ru.yaal.project.articlesaver.url.UrlWrapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Статья на сайте (страница сайта).
 * User: Aleks
 * Date: 09.09.13
 * Time: 9:54
 */
public interface IArticle {
    String getName() throws IOException;

    List<Resource> getResources() throws IOException;

    void save(File targetFolder) throws IOException;

    UrlWrapper getUrl();
}
