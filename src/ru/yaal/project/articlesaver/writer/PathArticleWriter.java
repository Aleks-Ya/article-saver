package ru.yaal.project.articlesaver.writer;

import org.apache.log4j.Logger;
import ru.yaal.project.articlesaver.article.IArticle;
import ru.yaal.project.articlesaver.parameters.IParameters;
import ru.yaal.project.articlesaver.resource.IResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;

/**
 * Сохраняет статью в файл на диске.
 * User: a.yablokov
 * Date: 08.11.13
 */
public class PathArticleWriter implements IArticleWriter {
    private static final Logger LOG = Logger.getLogger(PathArticleWriter.class);
    private final Path targetFolder;

    public PathArticleWriter(Path targetFolder) {
        this.targetFolder = targetFolder;
    }

    @Override
    public void save(IArticle article) throws ArticleWriteException {
        try {
            Path target = Paths.get(targetFolder.toAbsolutePath() + "\\" + article.getName() + ".html");
            LOG.debug(format("Сохраняю статью: \"%s\"", target.toAbsolutePath()));
            if (!target.toFile().exists()) {
                Files.write(target, Arrays.asList(article.getHtml()), Charset.forName("UTF-8"));
            } else {
                LOG.debug(format("%s уже загружен: %s", toString(), target));
            }
            List<IResource> resources = article.getResources();
            for (IResource resource : resources) {
                saveResource(resource);
            }
        } catch (IOException e) {
            throw new ArticleWriteException(e.getMessage(), e);
        }
    }

    private void saveResource(IResource resource) throws IOException {
        Path resourceDir = Paths.get(targetFolder.toAbsolutePath() + IParameters.RESOURCES_DIR);
        if (!resourceDir.toFile().exists()) {
            Files.createDirectories(resourceDir);
        }
        Path target = Paths.get(resourceDir.toAbsolutePath() + "\\" + resource.getFileName());
        if (!target.toFile().exists()) {
            try (InputStream is = new ByteArrayInputStream(resource.getContent())) {
                Files.copy(is, target);
            }
        } else {
            LOG.debug(format("%s уже загружен: %s", toString(), target));
        }
    }
}
