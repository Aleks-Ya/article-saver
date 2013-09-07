package ru.yaal.project.habrahabr.saver;

import java.nio.file.Path;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Aleks
 * Date: 07.09.13
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
public interface IParameters {
    String BASE_URL = "http://habrahabr.ru";
    String RESOURCES_DIR = "/resources";

    Path getTargetFolder();

    List<Article> getArticles();
}
