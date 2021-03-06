package ru.yaal.project.articlesaver.article;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import ru.yaal.project.articlesaver.resource.IResource;
import ru.yaal.project.articlesaver.parameters.IParameters;
import ru.yaal.project.articlesaver.url.UrlResolver;
import ru.yaal.project.articlesaver.url.UrlWrapper;

import java.util.List;

/**
 * Статья Habrahabr.
 * User: Aleks
 * Date: 03.09.13
 * Time: 6:52
 */
class HabrahabrArticle extends AbstractArticle {

    HabrahabrArticle(UrlWrapper url) {
        super(url, new UrlResolver("http://habrahabr.ru"));
    }

    @Override
    protected String fetchArticleHtml(HtmlPage page, List<IResource> resources) {
        HtmlDivision articleDiv = page.getFirstByXPath("//div[contains(@class,'content_left')]");
        HtmlElement head = page.getFirstByXPath("/html/head");
        StringBuilder newHtml = new StringBuilder();
        newHtml.append("<html>");
        newHtml.append(head.asXml());
        newHtml.append("<body>");
        newHtml.append(articleDiv.asXml());
        newHtml.append("<html><body>");
        String strHtml = newHtml.toString();
        for (IResource resource : resources) {
            strHtml = strHtml.replaceAll(resource.getOriginalUrl(),
                    "." + IParameters.RESOURCES_DIR + "/" + resource.getFileName());
        }
        return strHtml;
    }

    @Override
    protected String fetchArticleTitle(HtmlPage page) {
        HtmlSpan span = page.getFirstByXPath("//span[contains(@class, 'post_title')]");
        return span.asText();
    }

}
