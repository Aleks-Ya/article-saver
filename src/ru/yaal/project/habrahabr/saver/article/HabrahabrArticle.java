package ru.yaal.project.habrahabr.saver.article;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.log4j.Logger;
import ru.yaal.project.habrahabr.saver.Resource;
import ru.yaal.project.habrahabr.saver.url.UrlResolver;
import ru.yaal.project.habrahabr.saver.url.UrlWrapper;
import ru.yaal.project.habrahabr.saver.parameters.IParameters;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * Статья Habrahabr.
 * User: Aleks
 * Date: 03.09.13
 * Time: 6:52
 */
class HabrahabrArticle extends AbstractArticle {
    private static final Logger LOG = Logger.getLogger(HabrahabrArticle.class);
    private static final UrlResolver URL_RESOLVER = new UrlResolver("http://habrahabr.ru");

    HabrahabrArticle(UrlWrapper url) {
        super(url);
    }

    @Override
    protected HtmlPage loadPage(UrlWrapper url) throws IOException {
        LOG.debug(format("Загружаю статью: %s", url));
        final WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        HtmlPage result = webClient.getPage(url.toUrl());
        webClient.closeAllWindows();
        return result;
    }

    @Override
    protected String fetchArticleHtml(HtmlPage page, List<Resource> resources) {
        HtmlDivision articleDiv = page.getFirstByXPath("//div[contains(@class,'content_left')]");
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
        HtmlSpan titleSpan = page.getFirstByXPath("//span[contains(@class, 'post_title')]");
        return titleSpan.asText();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<Resource> fetchResources(HtmlPage page) throws MalformedURLException {
        List<Resource> resources = new ArrayList<>();
        for (HtmlScript script : (List<HtmlScript>) page.getByXPath("/html/head/script")) {
            String src = script.getSrcAttribute();
            addResource(src, resources);
        }
        for (HtmlLink link : (List<HtmlLink>) page.getByXPath("/html/head/link")) {
            addResource(link.getHrefAttribute(), resources);
        }
        for (HtmlImage image : (List<HtmlImage>) page.getByXPath("//img")) {
            addResource(image.getSrcAttribute(), resources);
        }
        return resources;
    }

    private void addResource(String src, List<Resource> resources) throws MalformedURLException {
        if (src != null && !src.isEmpty()) {
            resources.add(new Resource(src, URL_RESOLVER));
        }
    }

}
