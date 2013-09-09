package ru.yaal.project.habrahabr.saver;

import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class ResourceTest {
    @Test
    public void getFileName() throws Exception {
        UrlWrapper url = when(mock(UrlWrapper.class).getPath())
                .thenReturn("http://habrahabr.ru/i/habralogo.jpg").getMock();
        Resource resource = new Resource(url);
        assertEquals(resource.getFileName(), "69CC1A6F5584C1B7C8BCE851F142019FEF596281");
    }
}
