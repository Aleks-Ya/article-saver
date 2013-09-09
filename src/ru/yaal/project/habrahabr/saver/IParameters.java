package ru.yaal.project.habrahabr.saver;

import java.nio.file.Path;
import java.util.List;

/**
 * Параметры приложения: целевая папка и URL статей для загрузки.
 * User: Aleks
 * Date: 07.09.13
 * Time: 17:26
 */
public interface IParameters {
    String BASE_URL = "http://habrahabr.ru";
    String RESOURCES_DIR = "/resources";

    Path getTargetFolder();

    List<IArticle> getArticles();
}
