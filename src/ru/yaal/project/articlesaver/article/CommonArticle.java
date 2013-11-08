package ru.yaal.project.articlesaver.article;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import ru.yaal.project.articlesaver.resource.IResource;
import ru.yaal.project.articlesaver.url.UrlResolver;
import ru.yaal.project.articlesaver.url.UrlWrapper;

import java.util.List;

/**
 * Обычная статья (сохраняет html-страницу полностью).
 * User: Aleks
 * Date: 03.09.13
 * Time: 6:52
 */
class CommonArticle extends AbstractArticle {

    CommonArticle(UrlWrapper url) {
        super(url, new UrlResolver(""));
    }

    @Override
    protected String fetchArticleHtml(HtmlPage page, List<IResource> resources) {
        throw new UnsupportedOperationException("Не реализовано");
    }

    @Override
    protected String fetchArticleTitle(HtmlPage page) {
        throw new UnsupportedOperationException("Не реализовано");
    }

}
