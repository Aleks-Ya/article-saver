package ru.yaal.project.habrahabr.saver;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Статья на сайте (страница сайта).
 * User: Aleks
 * Date: 09.09.13
 * Time: 9:54
 */
public interface IArticle {
    List<Resource> getResources() throws IOException;

    String getName() throws IOException;

    void save(Path targetFolder) throws IOException;
}
