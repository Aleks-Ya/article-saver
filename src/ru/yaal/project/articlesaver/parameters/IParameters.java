package ru.yaal.project.articlesaver.parameters;

import ru.yaal.project.articlesaver.article.IArticle;

import java.io.File;
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

    File getTargetFolder();

    List<IArticle> getArticles();
}
