package ru.yaal.project.articlesaver.writer;

import org.apache.log4j.Logger;
import ru.yaal.project.articlesaver.Resource;
import ru.yaal.project.articlesaver.article.IArticle;

import java.io.IOException;
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
            List<Resource> resources = article.getResources();
            for (Resource resource : resources) {
                resource.save(targetFolder);
            }
        } catch (IOException e) {
            throw new ArticleWriteException(e.getMessage(), e);
        }
    }
}
