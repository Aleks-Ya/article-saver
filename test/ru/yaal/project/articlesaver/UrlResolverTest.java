package ru.yaal.project.articlesaver;

import org.testng.annotations.Test;
import ru.yaal.project.articlesaver.url.UrlResolver;
import ru.yaal.project.articlesaver.url.UrlWrapper;

import static org.testng.Assert.assertEquals;

public class UrlResolverTest {
    @Test
    public void testResolve() throws Exception {
        final UrlWrapper expected = new UrlWrapper("http://habrahabr.ru/post/192742/");
        UrlResolver resolver = new UrlResolver("http://habrahabr.ru");
        assertEquals(resolver.resolve("http://habrahabr.ru/post/192742/"), expected);
        assertEquals(resolver.resolve("/post/192742/"), expected);
        assertEquals(resolver.resolve("//habrahabr.ru/post/192742/"), expected);
    }
}
