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
 * ������ Habrahabr.
 * User: Aleks
 * Date: 03.09.13
 * Time: 6:52
 */
public class Article {
    private static final Logger LOG = Logger.getLogger(Article.class);
    private String html;
    private HtmlPage page;
    private List<Resource> resources = new ArrayList<>();

    public Article(URL url) throws IOException {
        page = loadPage(url);
        html = cleanUpArticle(page);
        determineResources(page);
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
            html = html.replaceAll(src, "." + Parameters.RESOURCES_DIR + "/" + resource.getFileName());
            resources.add(resource);
        }
    }

    private URL resolve(String url) throws MalformedURLException {
        String fullUrl;
        if (url.startsWith("//")) {
            fullUrl = "http:" + url;
        } else if (url.startsWith("/")) {
            fullUrl = Parameters.BASE_URL + url;
        } else {
            fullUrl = url;
        }
        LOG.debug(format("������ ������: %s", fullUrl));
        return new URL(fullUrl);
    }

    private HtmlPage loadPage(URL postUrl) throws IOException {
        LOG.info(format("�������� ������: %s", postUrl));
        final WebClient webClient = new WebClient();
        HtmlPage result = webClient.getPage(postUrl);
        webClient.closeAllWindows();
        return result;
    }

    /**
     * ������� HTML-������: ��������� ������ ���������� ������ � �����������.
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

    public List<Resource> getResources() {
        return resources;
    }

    public String getFileName() {
        HtmlSpan titleSpan = page.getFirstByXPath("//span[contains(@class, 'post_title')]");
        return titleSpan.asText() + ".html";
    }

    public void save(Path targetFolder) throws IOException {
        Path target = Paths.get(targetFolder.toAbsolutePath() + "\\" + getFileName());
        LOG.info(format("�������� ������: %s", target.toAbsolutePath()));
        if (!target.toFile().exists()) {
            Files.write(target, Arrays.asList(html), Charset.forName("UTF-8"));
        } else {
            LOG.debug(format("%s ��� ��������: %s", toString(), target));
        }
    }
}