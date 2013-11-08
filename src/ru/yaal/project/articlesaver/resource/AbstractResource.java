package ru.yaal.project.articlesaver.resource;

import static java.lang.String.format;

/**
 * Ресурс, на который ссылается html-страница со статьей (изображение, CSS, JavaScript).
 * User: Aleks
 * Date: 03.09.13
 * Time: 7:09
 */
abstract class AbstractResource implements IResource {

    @Override
    final public String toString() {
        return format("[Ресурс originalUrl=%s]", getOriginalUrl());
    }

}
