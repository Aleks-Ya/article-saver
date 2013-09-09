package ru.yaal.project.habrahabr.saver.article;

import ru.yaal.project.habrahabr.saver.Resource;
import ru.yaal.project.habrahabr.saver.UrlWrapper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Обычная статья (сохраняет html-страницу полностью).
 * User: Aleks
 * Date: 03.09.13
 * Time: 6:52
 */
public class CommonArticle implements IArticle {
    protected final UrlWrapper url;

    public CommonArticle(UrlWrapper url) {
        this.url = url;
    }

    @Override
    public List<Resource> getResources() throws IOException {
        throw new UnsupportedOperationException("Не реализовано");
    }

    @Override
    public String getName() throws IOException {
        throw new UnsupportedOperationException("Не реализовано");
    }

    @Override
    public void save(Path targetFolder) throws IOException {
        throw new UnsupportedOperationException("Не реализовано");
    }
}
