package ru.yaal.project.articlesaver.resource;

import org.apache.log4j.Logger;
import ru.yaal.project.articlesaver.url.UrlResolver;
import ru.yaal.project.articlesaver.url.UrlWrapper;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.lang.String.format;

/**
 * Ресурс, на который ссылается html-страница со статьей (изображение, CSS, JavaScript).
 * User: Aleks
 * Date: 03.09.13
 * Time: 7:09
 */
public final class Resource extends AbstractResource {
    private static final Logger LOG = Logger.getLogger(Resource.class);
    private final String originalUrl;
    private final UrlWrapper fullUrl;
    private byte[] content;

    public Resource(String originalUrl, UrlResolver resolver) throws MalformedURLException {
        this.originalUrl = originalUrl;
        this.fullUrl = resolver.resolve(originalUrl);
    }

    @Override
    public String getOriginalUrl() {
        return originalUrl;
    }

    @Override
    public String getFileName() {
        String result;
        try {
            MessageDigest digest = MessageDigest.getInstance("sha-1");
            digest.update(fullUrl.toUrlString().getBytes());
            result = hashToString(digest.digest()).replaceAll("\\s*", "");
        } catch (NoSuchAlgorithmException e) {
            LOG.error(format("Алгоритм sha-1 не поддерживается, использую Object#hashCode()."), e);
            result = String.valueOf(fullUrl.toUrlString().hashCode());
        }
        return result;
    }

    private String hashToString(byte[] hash) {
        String result = "";
        for (byte b : hash) {
            int v = b & 0xFF;
            if (v < 16) result += "0";
            result += Integer.toString(v, 16).toUpperCase() + " ";
        }
        return result;
    }

    @Override
    public byte[] getContent() throws IOException {
        if (content == null) {
            try (BufferedInputStream is = new BufferedInputStream(fullUrl.openStream())) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int n;
                byte[] data = new byte[10000];
                while ((n = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, n);
                }
                content = buffer.toByteArray();
            }
        }
        return content;
    }
}
