package ru.yaal.project.habrahabr.saver.article;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import ru.yaal.project.habrahabr.saver.Resource;
import ru.yaal.project.habrahabr.saver.url.UrlWrapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * ������� ������ (��������� html-�������� ���������).
 * User: Aleks
 * Date: 03.09.13
 * Time: 6:52
 */
class CommonArticle extends AbstractArticle {

    CommonArticle(UrlWrapper url) {
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
