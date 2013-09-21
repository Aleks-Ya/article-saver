package ru.yaal.project.articlesaver.article;

import ru.yaal.project.articlesaver.url.UrlWrapper;

/**
 * Фабрика IArticle.
 * По URL статьи определяет сайт и возвращает подходящую реализацию IArticle.
 * User: Aleks
 * Date: 09.09.13
 * Time: 10:16
 */
public class ArticleFactory {
    public static IArticle getArticle(UrlWrapper url) {
        String host = url.getHost().replaceFirst(".*\\.(.+\\..+)","$1");//Оставляет домен 2-го уровня
        switch (host) {
            case "habrahabr.ru": {
                return new HabrahabrArticle(url);
            }
            case "wikipedia.org": {
                return new WikipediaArticle(url);
            }
            case "hh.ru": {
                return new HeadHunterArticle(url);
            }
            default: {
                return new CommonArticle(url);
            }
        }
    }
}