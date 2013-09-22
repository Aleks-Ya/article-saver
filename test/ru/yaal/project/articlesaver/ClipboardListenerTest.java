package ru.yaal.project.articlesaver;

import org.testng.annotations.Test;
import ru.yaal.project.articlesaver.article.ArticleLoader;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

import static org.mockito.Mockito.*;

public class ClipboardListenerTest {
    @Test
    public void testRun() throws Exception {
        ClipboardListener cl = new ClipboardListener(mock(ArticleLoader.class));
        Clipboard clipboard = spy(Toolkit.getDefaultToolkit().getSystemClipboard());
        Transferable transferable = mock(Transferable.class);
        ClipboardOwner newOwner = mock(ClipboardOwner.class);
        clipboard.setContents(transferable, newOwner);
        verify(clipboard).setContents(transferable, newOwner);
        cl.interrupt();
    }
}
