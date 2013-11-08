package ru.yaal.project.articlesaver.article;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import ru.yaal.project.articlesaver.resource.IResource;
import ru.yaal.project.articlesaver.parameters.IParameters;
import ru.yaal.project.articlesaver.url.UrlResolver;
import ru.yaal.project.articlesaver.url.UrlWrapper;

import java.util.List;

/**
 * Статья Wikipedia.
 * User: Aleks
 * Date: 09.09.13
 * Time: 11:20
 */
class WikipediaArticle extends AbstractArticle {
    WikipediaArticle(UrlWrapper url) {
        super(url, new UrlResolver("http://ru.wikipedia.org"));
    }

    @Override
    protected String fetchArticleHtml(HtmlPage page, List<IResource> resources) {
        HtmlDivision articleDiv = (HtmlDivision) page.getElementById("content");
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
        return page.getElementById("firstHeading").asText();
    }

}
