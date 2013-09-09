package ru.yaal.project.habrahabr.saver;

import org.apache.log4j.Logger;
import ru.yaal.project.habrahabr.saver.parameters.IParameters;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.lang.String.format;

/**
 * Ресурс, на который ссылается html-страница со статьей (изображение, CSS, JavaScript).
 * User: Aleks
 * Date: 03.09.13
 * Time: 7:09
 */
public final class Resource {
    private static final Logger LOG = Logger.getLogger(Resource.class);
    private final UrlWrapper url;

    public Resource(UrlWrapper url) {
        this.url = url;
    }

    public void save(Path targetFolder) throws IOException {
        Path resourceDir = Paths.get(targetFolder.toAbsolutePath() + IParameters.RESOURCES_DIR);
        if (!resourceDir.toFile().exists()) {
            Files.createDirectories(resourceDir);
        }
        Path target = Paths.get(resourceDir.toAbsolutePath() + "\\" + getFileName());
        if (!target.toFile().exists()) {
            try (InputStream is = url.openStream()) {
                Files.copy(is, target);
            }
        } else {
            LOG.debug(format("%s уже загружен: %s", toString(), target));
        }
    }

    public String getFileName() {
        String result;
        try {
            MessageDigest digest = MessageDigest.getInstance("sha-1");
            digest.update(url.getPath().getBytes());
            result = hashToString(digest.digest()).replaceAll("\\s*", "");
        } catch (NoSuchAlgorithmException e) {
            LOG.error(format("Алгоритм sha-1 не поддерживается, использую Object#hashCode()."), e);
            result = String.valueOf(url.getPath().hashCode());
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
        return format("[Ресурс url=%s]", url);
    }
}
