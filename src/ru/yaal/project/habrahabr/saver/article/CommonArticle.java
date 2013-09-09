package ru.yaal.project.habrahabr.saver.article;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import ru.yaal.project.habrahabr.saver.Resource;
import ru.yaal.project.habrahabr.saver.url.UrlResolver;
import ru.yaal.project.habrahabr.saver.url.UrlWrapper;

import java.util.List;

/**
 * ������� ������ (��������� html-�������� ���������).
 * User: Aleks
 * Date: 03.09.13
 * Time: 6:52
 */
class CommonArticle extends AbstractArticle {

    CommonArticle(UrlWrapper url) {
        super(url, new UrlResolver(""));
    }

    @Override
    protected String fetchArticleHtml(HtmlPage page, List<Resource> resources) {
        throw new UnsupportedOperationException("�� �����������");
    }

    @Override
    protected String fetchArticleTitle(HtmlPage page) {
        throw new UnsupportedOperationException("�� �����������");
    }

}
