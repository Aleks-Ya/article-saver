package ru.yaal.project.articlesaver.article;

import ru.yaal.project.articlesaver.resource.IResource;
import ru.yaal.project.articlesaver.url.UrlWrapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Статья на сайте (страница сайта).
 * User: Aleks
 * Date: 09.09.13
 * Time: 9:54
 */
public interface IArticle {
    /**
     * Возвращает название статьи.
     */
    String getName() throws IOException;

    /**
     * Возвращает ресурсы, на которые ссылается статья.
     */
    List<? extends IResource> getResources() throws IOException;

    /**
     * Возвращает HTML-код статьи.
     */
    String getHtml();

    /**
     * Возвращает url, с которого была скачана статья.
     */
    UrlWrapper getUrl() throws MalformedURLException;
}
