package ru.yaal.project.articlesaver.writer;

import java.io.IOException;

/**
 * Ошибка при записи статьи.
 * User: a.yablokov
 * Date: 08.11.13
 */
class ArticleWriteException extends Exception {
    public ArticleWriteException(String message, IOException cause) {
        super(message, cause);
    }

    public ArticleWriteException(String format, Object... args) {
        super(String.format(format, args));
    }
}
