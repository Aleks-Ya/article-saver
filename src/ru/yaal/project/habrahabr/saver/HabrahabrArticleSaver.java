package ru.yaal.project.habrahabr.saver;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.log4j.Logger;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import static java.lang.String.format;

/**
 * Created with IntelliJ IDEA.
 * User: Aleks
 * Date: 30.08.13
 * Time: 6:43
 * To change this template use File | Settings | File Templates.
 */
public class HabrahabrArticleSaver {
    private static final Logger LOG = Logger.getLogger(HabrahabrArticleSaver.class);
    private static final String BASE_URL = "http://habrahabr.ru";
    private static final String URL_TEMPLATE = BASE_URL + "/post/%s/";
    private static File destFolder;

    /**
     * @param args Номера постов (например, чтобы скачать "http://habrahabr.ru/post/157165/", нужно ввести "157165").
     */
    public static void main(String[] args) {
        verifyParameters(args);
        destFolder = new File(args[0]);
        String[] postIds = Arrays.copyOfRange(args, 1, args.length);
        for (String postId : postIds) {
            LOG.info(format("Обрабатываю пост №%s.", postId));
            try {
                String content = loadHabrahabrPost(postId);
                File file = saveHtmlToFile("c:/temp/habr_out.html", content);
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                LOG.warn(format("Ошибка при подключении к URL: %s. Пропускаю.", postId), e);
            }
        }
    }

    private static void verifyParameters(String args[]) {
        if (args == null || args.length < 2) {
            throw new IllegalArgumentException("Не указаны параметры приложения: habrsaver d:/habr_articles 156395 183494");
        } else {
            File destFolder = new File(args[0]);
            if (!destFolder.exists()) {
                throw new IllegalArgumentException(format("Целевая папка \"%s\" не существует.", destFolder));
            }
            if (!destFolder.isDirectory()) {
                throw new IllegalArgumentException(format("Целевая папка \"%s\" является файлом.", destFolder));
            }
        }
    }

    /**
     * Сохраняет контент в файл.
     *
     * @param fileName
     * @param content
     */
    private static File saveHtmlToFile(String fileName, CharSequence content) throws IOException {
        File file = new File(fileName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(content.toString());
        writer.close();
        return file;
    }

    private static String loadHabrahabrPost(String postId) throws IOException {
        String urlStr = format(URL_TEMPLATE, postId);
        URL postUrl = new URL(urlStr);
        final WebClient webClient = new WebClient();
        final HtmlPage postPage = webClient.getPage(postUrl);
        HtmlDivision articleDiv = postPage.getFirstByXPath("//div[contains(@class,'content_left')]");
        HtmlElement head = postPage.getFirstByXPath("/html/head");
        loadScripts(head);
        StringBuilder newHtml = new StringBuilder();
        newHtml.append("<html>");
        newHtml.append(head.asXml());
        newHtml.append("<body>");
        newHtml.append(articleDiv.asXml());
        newHtml.append("<html><body>");
        webClient.closeAllWindows();
        return newHtml.toString();
    }

    private static void loadScripts(HtmlElement element) {
        DomNodeList<HtmlElement> scripts = element.getElementsByTagName("script");
        for (HtmlElement script : scripts) {
            try {
                String fullUrl = BASE_URL + script.getAttribute("src");
                URL url = new URL(fullUrl);
                File targetFile = new File(destFolder, url.getFile().replaceAll("^.*[\\\\/]", ""));
                loadFile(url, targetFile);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    private static void loadFile(URL source, File target) throws IOException {
        BufferedInputStream is = null;
        BufferedOutputStream os = null;
        try {
            URLConnection con = source.openConnection();
            is = new BufferedInputStream(con.getInputStream());
            os = new BufferedOutputStream(new FileOutputStream(target));
            int available;
            while ((available = is.available()) != 0) {
                byte[] input = new byte[available];
                is.read(input);
                os.write(input);
            }
        } finally {
            if (is != null) is.close();
            if (os != null) os.close();
        }
    }

    private static String replaceGlobalLinksWithLocal(String fullUrl) {
        return null;
    }
}
