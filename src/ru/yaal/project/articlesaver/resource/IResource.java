package ru.yaal.project.articlesaver.resource;

import java.io.IOException;

/**
 * Ресурс, на который ссылается html-страница со статьей (изображение, CSS, JavaScript).
 * User: a.yablokov
 * Date: 08.11.13
 */
public interface IResource {
    String getFileName();

    String getOriginalUrl();

    byte[] getContent() throws IOException;
}
