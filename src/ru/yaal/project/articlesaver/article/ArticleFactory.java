package ru.yaal.project.articlesaver.article;

import ru.yaal.project.articlesaver.url.Site;
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
        Site site = Site.resolve(url);
        switch (site) {
            case HABRAHABR: {
                return new HabrahabrArticle(url);
            }
            case WIKIPEDIA: {
                return new WikipediaArticle(url);
            }
            case HEAD_HUNTER: {
                return new HeadHunterArticle(url);
            }
            default: {
                return new CommonArticle(url);
            }
        }
    }
}
