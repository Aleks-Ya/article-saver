package ru.yaal.project.habrahabr.saver;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;

/**
 * Статья Habrahabr.
 * User: Aleks
 * Date: 03.09.13
 * Time: 6:52
 */
public class Article {
    private static final Logger LOG = Logger.getLogger(Article.class);
    private URL url;
    private String html;
    private HtmlPage page;
    private List<Resource> resources = new ArrayList<>();
    private boolean isLoaded = false;

    public Article(URL url) {
        this.url = url;
    }

    private void load() throws IOException {
        if (!isLoaded) {
            page = loadPage(url);
            html = cleanUpArticle(page);
            determineResources(page);
            isLoaded = true;
            LOG.info(format("Загружена статья: \"%s\"", getName()));
        }
    }

    @SuppressWarnings("unchecked")
    private void determineResources(HtmlPage page) throws MalformedURLException {
        for (HtmlScript script : (List<HtmlScript>) page.getByXPath("/html/head/script")) {
            String src = script.getSrcAttribute();
            addResource(src);
        }
        for (HtmlLink link : (List<HtmlLink>) page.getByXPath("/html/head/link")) {
            addResource(link.getHrefAttribute());
        }
        for (HtmlImage image : (List<HtmlImage>) page.getByXPath("//img")) {
            addResource(image.getSrcAttribute());
        }
    }

    private void addResource(String src) throws MalformedURLException {
        if (src != null && !src.isEmpty()) {
            Resource resource = new Resource(resolve(src));
            html = html.replaceAll(src, "." + IParameters.RESOURCES_DIR + "/" + resource.getFileName());
            resources.add(resource);
        }
    }

    private URL resolve(String url) throws MalformedURLException {
        String fullUrl;
        if (url.startsWith("//")) {
            fullUrl = "http:" + url;
        } else if (url.startsWith("/")) {
            fullUrl = IParameters.BASE_URL + url;
        } else {
            fullUrl = url;
        }
        LOG.debug(format("Найден ресурс: %s", fullUrl));
        return new URL(fullUrl);
    }

    private HtmlPage loadPage(URL postUrl) throws IOException {
        LOG.debug(format("Загружаю статью: %s", postUrl));
        final WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        HtmlPage result = webClient.getPage(postUrl);
        webClient.closeAllWindows();
        return result;
    }

    /**
     * Очистка HTML-статьи: оставляет только содержание статьи и комментарии.
     */
    private String cleanUpArticle(HtmlPage page) {
        HtmlDivision articleDiv = page.getFirstByXPath("//div[contains(@class,'content_left')]");
        HtmlElement head = page.getFirstByXPath("/html/head");
        StringBuilder newHtml = new StringBuilder();
        newHtml.append("<html>");
        newHtml.append(head.asXml());
        newHtml.append("<body>");
        newHtml.append(articleDiv.asXml());
        newHtml.append("<html><body>");
        return newHtml.toString();
    }

    public List<Resource> getResources() throws IOException {
        load();
        return resources;
    }

    public String getName() throws IOException {
        load();
        HtmlSpan titleSpan = page.getFirstByXPath("//span[contains(@class, 'post_title')]");
        return titleSpan.asText();
    }

    public void save(Path targetFolder) throws IOException {
        Path target = Paths.get(targetFolder.toAbsolutePath() + "\\" + getName() + ".html");
        LOG.debug(format("Сохраняю статью: \"%s\"", target.toAbsolutePath()));
        if (!target.toFile().exists()) {
            Files.write(target, Arrays.asList(html), Charset.forName("UTF-8"));
        } else {
            LOG.debug(format("%s уже загружен: %s", toString(), target));
        }
    }

    @Override
    public String toString() {
        return format("[Article url=%s]", url);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Article) {
            Article otherArticle = (Article) other;
            return url.equals(otherArticle.url);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}
