package ru.yaal.project.articlesaver.article;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import ru.yaal.project.articlesaver.Resource;
import ru.yaal.project.articlesaver.parameters.IParameters;
import ru.yaal.project.articlesaver.url.UrlResolver;
import ru.yaal.project.articlesaver.url.UrlWrapper;

import java.util.List;

/**
 * Скачивает статью HeadHunter.ru
 * User: Aleks
 * Date: 17.09.13
 * Time: 15:00
 */
public class HeadHunterArticle extends AbstractArticle {
    HeadHunterArticle(UrlWrapper url) {
        super(url, new UrlResolver("http://hh.ru/"));
    }

    @Override
    protected String fetchArticleHtml(HtmlPage page, List<Resource> resources) {
        HtmlDivision articleDiv = page.getFirstByXPath("//div[contains(@class,'nopaddings')]");
        HtmlElement head = page.getFirstByXPath("/html/head");
        StringBuilder newHtml = new StringBuilder();
        newHtml.append("<html>");
        newHtml.append(head.asXml());
        newHtml.append("<body>");
        newHtml.append(articleDiv.asXml());
        newHtml.append("<html><body>");
        String strHtml = newHtml.toString();
        for (Resource resource : resources) {
            strHtml = strHtml.replaceAll(resource.getOriginalUrl(),
                    "." + IParameters.RESOURCES_DIR + "/" + resource.getFileName());
        }
        return strHtml;
    }

    @Override
    protected String fetchArticleTitle(HtmlPage page) {
        return page.getTitleText();
    }
}
