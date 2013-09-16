package ru.yaal.project.articlesaver.url;

import org.apache.log4j.Logger;

import java.net.MalformedURLException;

import static java.lang.String.format;

/**
 * Разрешает относительный URL к базовому URL.
 * User: Aleks
 * Date: 09.09.13
 * Time: 12:11
 */
public class UrlResolver {
    private static final Logger LOG = Logger.getLogger(UrlResolver.class);
    private final String baseUrl;

    public UrlResolver(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public UrlWrapper resolve(String url) throws MalformedURLException {
        String fullUrl;
        if (url.startsWith("//")) {
            fullUrl = "http:" + url;
        } else if (url.startsWith("/")) {
            fullUrl = baseUrl + url;
        } else {
            fullUrl = url;
        }
        LOG.debug(format("Найден ресурс: %s", fullUrl));
        return new UrlWrapper(fullUrl);
    }
}
