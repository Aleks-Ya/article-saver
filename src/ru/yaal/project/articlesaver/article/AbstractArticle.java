package ru.yaal.project.articlesaver.article;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import org.apache.log4j.Logger;
import ru.yaal.project.articlesaver.resource.IResource;
import ru.yaal.project.articlesaver.resource.Resource;
import ru.yaal.project.articlesaver.url.UrlResolver;
import ru.yaal.project.articlesaver.url.UrlWrapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * Обычная статья (сохраняет html-страницу полностью).
 * User: Aleks
 * Date: 03.09.13
 * Time: 6:52
 */
public abstract class AbstractArticle implements IArticle {
    private static final Logger LOG = Logger.getLogger(AbstractArticle.class);
    final UrlWrapper url;
    private final UrlResolver resolver;
    private String articleHtml;
    private String articleTitle;
    private List<IResource> resources;
    private boolean isLoaded = false;

    AbstractArticle(UrlWrapper url, UrlResolver resolver) {
        this.url = url;
        this.resolver = resolver;
    }

    @Override
    public String getHtml() {
        return articleHtml;
    }

    @Override
    public final List<? extends IResource> getResources() throws IOException {
        load();
        return resources;
    }

    final void load() throws IOException {
        if (!isLoaded) {
            HtmlPage page = loadPage(url);
            resources = fetchResources(page);
            articleHtml = fetchArticleHtml(page, resources);
            articleTitle = fetchArticleTitle(page);
            isLoaded = true;
            LOG.info(format("Загружена статья: \"%s\"", getName()));
        }
    }

    private HtmlPage loadPage(UrlWrapper url) throws IOException {
        LOG.debug(format("Загружаю статью: %s", url));
        final WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        HtmlPage result = webClient.getPage(url.toUrl());
        webClient.closeAllWindows();
        return result;
    }

    protected abstract String fetchArticleHtml(HtmlPage page, List<IResource> resources);

    protected abstract String fetchArticleTitle(HtmlPage page);

    @Override
    public final String getName() throws IOException {
        load();
        return articleTitle;
    }

    @Override
    public String toString() {
        return format("[%s url=%s]", getClass().getName(), url);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof HabrahabrArticle) {
            HabrahabrArticle otherArticle = (HabrahabrArticle) other;
            return url.equals(otherArticle.url);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    @SuppressWarnings("unchecked")
    private List<IResource> fetchResources(HtmlPage page) throws MalformedURLException {
        List<IResource> resources = new ArrayList<>();
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

    @Override
    public UrlWrapper getUrl() {
        return url;
    }

    private void addResource(String src, List<IResource> resources) throws MalformedURLException {
        if (src != null && !src.isEmpty()) {
            resources.add(new Resource(src, resolver));
        }
    }

}
