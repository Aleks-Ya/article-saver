package ru.yaal.project.habrahabr.saver.article;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import ru.yaal.project.habrahabr.saver.Resource;
import ru.yaal.project.habrahabr.saver.UrlWrapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * ������ Wikipedia.
 * User: Aleks
 * Date: 09.09.13
 * Time: 11:20
 */
class WikipediaArticle extends AbstractArticle {
    WikipediaArticle(UrlWrapper url) {
        super(url);
    }

    @Override
    protected HtmlPage loadPage(UrlWrapper url) throws IOException {
        throw new UnsupportedOperationException("�� �����������");
    }

    @Override
    protected String fetchArticleHtml(HtmlPage page, List<Resource> resources) {
        throw new UnsupportedOperationException("�� �����������");
    }

    @Override
    protected String fetchArticleTitle(HtmlPage page) {
        throw new UnsupportedOperationException("�� �����������");
    }

    @Override
    protected List<Resource> fetchResources(HtmlPage page) throws MalformedURLException {
        throw new UnsupportedOperationException("�� �����������");
    }

}
