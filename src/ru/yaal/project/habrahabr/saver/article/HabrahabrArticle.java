package ru.yaal.project.habrahabr.saver.article;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.log4j.Logger;
import ru.yaal.project.habrahabr.saver.Resource;
import ru.yaal.project.habrahabr.saver.UrlWrapper;
import ru.yaal.project.habrahabr.saver.parameters.IParameters;

import java.io.IOException;
import java.net.MalformedURLException;
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
public class HabrahabrArticle extends CommonArticle {
    private static final Logger LOG = Logger.getLogger(HabrahabrArticle.class);
    private final List<Resource> resources = new ArrayList<>();
    private String html;
    private HtmlPage page;
    private boolean isLoaded = false;

    public HabrahabrArticle(UrlWrapper url) {
        super(url);
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

    private UrlWrapper resolve(String url) throws MalformedURLException {
        String fullUrl;
        if (url.startsWith("//")) {
            fullUrl = "http:" + url;
        } else if (url.startsWith("/")) {
            fullUrl = IParameters.BASE_URL + url;
        } else {
            fullUrl = url;
        }
        LOG.debug(format("Найден ресурс: %s", fullUrl));
        return new UrlWrapper(fullUrl);
    }

    private HtmlPage loadPage(UrlWrapper postUrl) throws IOException {
        LOG.debug(format("Загружаю статью: %s", postUrl));
        final WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        HtmlPage result = webClient.getPage(postUrl.toUrl());
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

    @Override
    public List<Resource> getResources() throws IOException {
        load();
        return resources;
    }

    @Override
    public String getName() throws IOException {
        load();
        HtmlSpan titleSpan = page.getFirstByXPath("//span[contains(@class, 'post_title')]");
        return titleSpan.asText();
    }

    @Override
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
        return format("[HabrahabrArticle url=%s]", url);
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
}
