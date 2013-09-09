package ru.yaal.project.habrahabr.saver.article;

import ru.yaal.project.habrahabr.saver.url.UrlWrapper;

/**
 * ������� IArticle.
 * �� URL ������ ���������� ���� � ���������� ���������� ���������� IArticle.
 * User: Aleks
 * Date: 09.09.13
 * Time: 10:16
 */
public class ArticleFactory {
    public static IArticle getArticle(UrlWrapper url) {
        String host = url.getHost();
        switch (host) {
            case "habrahabr.ru": {
                return new HabrahabrArticle(url);
            }
            case "ru.wikipedia.org": {
                return new WikipediaArticle(url);
            }
            default: {
                return new CommonArticle(url);
            }
        }
    }
}
