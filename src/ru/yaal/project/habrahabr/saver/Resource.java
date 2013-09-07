package ru.yaal.project.habrahabr.saver;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;

/**
 * Ресурс, на который ссылается html-страница со статьей (изображение, CSS, JavaScript).
 * User: Aleks
 * Date: 03.09.13
 * Time: 7:09
 */
public class Resource {
    private static final Logger LOG = Logger.getLogger(Resource.class);
    private URL url;

    public Resource(URL url) {
        this.url = url;
    }

    public void load(Path targetFolder) throws IOException {
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
        String[] splitted = url.getPath().split("/");
        return splitted[splitted.length - 1];
    }

    @Override
    public String toString() {
        return format("[Ресурс url=%s]", url);
    }
}
