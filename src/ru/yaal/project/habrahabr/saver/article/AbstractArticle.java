package ru.yaal.project.habrahabr.saver.article;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.log4j.Logger;
import ru.yaal.project.habrahabr.saver.Resource;
import ru.yaal.project.habrahabr.saver.UrlWrapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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
    private String articleHtml;
    private String articleTitle;
    private List<Resource> resources;
    private boolean isLoaded = false;

    AbstractArticle(UrlWrapper url) {
        this.url = url;
    }

    @Override
    public final List<Resource> getResources() throws IOException {
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

    protected abstract HtmlPage loadPage(UrlWrapper url) throws IOException;

    protected abstract String fetchArticleHtml(HtmlPage page, List<Resource> resources);

    protected abstract String fetchArticleTitle(HtmlPage page);

    protected abstract List<Resource> fetchResources(HtmlPage page) throws MalformedURLException;

    @Override
    public final String getName() throws IOException {
        load();
        return articleTitle;
    }

    @Override
    public final void save(Path targetFolder) throws IOException {
        Path target = Paths.get(targetFolder.toAbsolutePath() + "\\" + getName() + ".html");
        LOG.debug(format("Сохраняю статью: \"%s\"", target.toAbsolutePath()));
        if (!target.toFile().exists()) {
            Files.write(target, Arrays.asList(articleHtml), Charset.forName("UTF-8"));
        } else {
            LOG.debug(format("%s уже загружен: %s", toString(), target));
        }
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

}
