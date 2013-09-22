package ru.yaal.project.articlesaver;

import org.apache.log4j.Logger;
import ru.yaal.project.articlesaver.article.ArticleFactory;
import ru.yaal.project.articlesaver.article.ArticleLoader;
import ru.yaal.project.articlesaver.url.Site;
import ru.yaal.project.articlesaver.url.UrlWrapper;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.BufferedReader;
import java.io.IOException;

import static java.lang.String.format;

/**
 * Слушатель буфера обмена.<br/>
 * Когда в буфер копируется URL поддерживаемого сайта {@link ru.yaal.project.articlesaver.url.Site},
 * начинает его загрузку.<br/>
 * Остановка потока: {@code ClipboardListener#interrupt()}.<br/>
 * User: Aleks
 * Date: 21.09.13
 * Time: 11:29
 */
public class ClipboardListener extends Thread implements ClipboardOwner {
    private static final Logger LOG = Logger.getLogger(ClipboardListener.class);
    private final ArticleLoader loader;

    public ClipboardListener(ArticleLoader loader) {
        this.loader = loader;
        setName("ClipboardListener");
        start();
    }

    @Override
    public void run() {
        regainOwnership(Toolkit.getDefaultToolkit().getSystemClipboard());
        while (!interrupted()) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                LOG.debug(e.getMessage(), e);
            }
        }
    }

    @Override
    public void lostOwnership(Clipboard cb, Transferable tt) {
        LOG.trace(format("lostOwnership cb=%s, tt=%s", cb, tt));
        while (true) {
            try {
                Transferable t = cb.getContents(null);
                LOG.trace(format("Обрабатываю Transferable: %s", t));
                process(t);
                regainOwnership(cb);
                return;
            } catch (IllegalStateException e) {
                LOG.trace(e.getMessage(), e);//trace п.ч. часто падает и не является ошибкой (напр, по PrintScreen)
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ie) {
                    LOG.error(ie.getMessage(), ie);
                }
            }
        }
    }

    private void process(Transferable t) {
        final DataFlavor df = DataFlavor.stringFlavor;
        if (t.isDataFlavorSupported(df)) {
            try {
                BufferedReader r = new BufferedReader(df.getReaderForText(t));
                String line;
                while ((line = r.readLine()) != null) {
                    if (Site.resolve(line) != Site.UNKNOWN) {
                        LOG.trace(format("Из буфера обмена прочитан URL %s", line));
                        loader.load(ArticleFactory.getArticle(new UrlWrapper(line)));
                    } else {
                        LOG.trace(format("Строка не является URL: %s", line));
                    }
                }
                r.close();
            } catch (UnsupportedFlavorException | IOException | InterruptedException e) {
                LOG.error(e.getMessage(), e);
            }
        } else {
            LOG.trace("Содержимое буфера обмена проигнорировано.");
        }
    }

    private void regainOwnership(Clipboard cb) {
        LOG.trace(format("regainOwnership cb=%s", cb));
        cb.setContents(cb.getContents(null), this);
    }
}
