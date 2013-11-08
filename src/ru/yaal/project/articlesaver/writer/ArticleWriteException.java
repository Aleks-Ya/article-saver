package ru.yaal.project.articlesaver.writer;

import java.io.IOException;

/**
 * Ошибка при записи статьи.
 * User: a.yablokov
 * Date: 08.11.13
 */
public class ArticleWriteException extends Exception {
    public ArticleWriteException(String message, IOException cause) {
        super(message, cause);
    }
}
