package ru.yaal.project.habrahabr.saver;

import org.apache.log4j.Logger;
import ru.yaal.project.habrahabr.saver.parameters.IParameters;
import ru.yaal.project.habrahabr.saver.url.UrlResolver;
import ru.yaal.project.habrahabr.saver.url.UrlWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.lang.String.format;

/**
 * ������, �� ������� ��������� html-�������� �� ������� (�����������, CSS, JavaScript).
 * User: Aleks
 * Date: 03.09.13
 * Time: 7:09
 */
public final class Resource {
    private static final Logger LOG = Logger.getLogger(Resource.class);
    private final String originalUrl;
    private final UrlWrapper fullUrl;

    public Resource(String originalUrl, UrlResolver resolver) throws MalformedURLException {
        this.originalUrl = originalUrl;
        this.fullUrl = resolver.resolve(originalUrl);
    }

    public void save(Path targetFolder) throws IOException {
        Path resourceDir = Paths.get(targetFolder.toAbsolutePath() + IParameters.RESOURCES_DIR);
        if (!resourceDir.toFile().exists()) {
            Files.createDirectories(resourceDir);
        }
        Path target = Paths.get(resourceDir.toAbsolutePath() + "\\" + getFileName());
        if (!target.toFile().exists()) {
            try (InputStream is = fullUrl.openStream()) {
                Files.copy(is, target);
            }
        } else {
            LOG.debug(format("%s ��� ��������: %s", toString(), target));
        }
    }

    public String getFileName() {
        String result;
        try {
            MessageDigest digest = MessageDigest.getInstance("sha-1");
            digest.update(fullUrl.toUrlString().getBytes());
            result = hashToString(digest.digest()).replaceAll("\\s*", "");
        } catch (NoSuchAlgorithmException e) {
            LOG.error(format("�������� sha-1 �� ��������������, ��������� Object#hashCode()."), e);
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
    public String toString() {
        return format("[������ originalUrl=%s]", originalUrl);
    }

    public String getOriginalUrl() {
        return originalUrl;
    }
}
