package ru.yaal.project.articlesaver;

import org.testng.annotations.Test;
import ru.yaal.project.articlesaver.resource.IResource;
import ru.yaal.project.articlesaver.resource.Resource;
import ru.yaal.project.articlesaver.url.UrlResolver;
import ru.yaal.project.articlesaver.url.UrlWrapper;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class ResourceTest {
    @Test
    public void getFileName() throws Exception {
        UrlResolver resolver = when(mock(UrlResolver.class).resolve(anyString()))
                .thenReturn(new UrlWrapper("http://habrahabr.ru/i/habralogo.jpg")).getMock();
        IResource resource = new Resource("http://habrahabr.ru/i/habralogo.jpg", resolver);
        assertEquals(resource.getFileName(), "69CC1A6F5584C1B7C8BCE851F142019FEF596281");
    }
}
