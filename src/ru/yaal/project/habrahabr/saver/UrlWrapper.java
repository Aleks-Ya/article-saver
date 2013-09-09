package ru.yaal.project.habrahabr.saver;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.String.format;

/**
 * Обертка для {link java.net.URL}.
 * Для возможности создания mock для final-класса URL.
 * User: Aleks
 * Date: 09.09.13
 * Time: 9:34
 */
public class UrlWrapper {
    private URL url;

    public UrlWrapper(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    public final InputStream openStream() throws IOException {
        return url.openStream();
    }

    public String toUrlString() {
        return url.toString();
    }

    @Override
    public String toString() {
        return format("[UrlWrapper url=%s]", url.toString());
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof UrlWrapper) {
            UrlWrapper otherUrlWrapper = (UrlWrapper) other;
            return otherUrlWrapper.toUrl().equals(toUrl());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    public String getHost() {
        return url.getHost();
    }

    public URL toUrl() {
        return url;
    }
}
