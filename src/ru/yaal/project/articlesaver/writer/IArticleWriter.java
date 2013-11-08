package ru.yaal.project.articlesaver.writer;

import ru.yaal.project.articlesaver.article.IArticle;

/**
 * Сохраняет статью (файл, БД и т.д.)
 * User: a.yablokov
 * Date: 08.11.13
 */
public interface IArticleWriter {
    void save(IArticle article) throws ArticleWriteException;
}
