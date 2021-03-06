package ru.yaal.project.articlesaver.url;

import org.apache.log4j.Logger;

import java.net.MalformedURLException;

/**
 * Поддерживаемые сайты.
 * User: Aleks
 * Date: 21.09.13
 * Time: 11:57
 */
public enum Site {
    HABRAHABR("habrahabr.ru"), WIKIPEDIA("wikipedia.org"), HEAD_HUNTER("hh.ru"), UNKNOWN("");
    private static final Logger LOG = Logger.getLogger(Site.class);
    private final String host;

    private Site(String host) {
        this.host = host;
    }

    public static Site resolve(UrlWrapper url) {
        String urlHost = url.getHost().replaceFirst(".*\\.(.+\\..+)/?.*", "$1");//Оставляет домен 2-го уровня
        if (HABRAHABR.host.equals(urlHost)) {
            return HABRAHABR;
        } else if (WIKIPEDIA.host.equals(urlHost)) {
            return WIKIPEDIA;
        } else if (HEAD_HUNTER.host.equals((urlHost))) {
            return HEAD_HUNTER;
        } else {
            return UNKNOWN;
        }
    }

    public static Site resolve(String url) {
        try {
            return resolve(new UrlWrapper(url));
        } catch (MalformedURLException e) {
            LOG.debug(e.getMessage(), e);
            return UNKNOWN;
        }
    }
}
