package ru.yaal.project.articlesaver.writer;

import org.apache.log4j.BasicConfigurator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.yaal.project.articlesaver.article.ArticleEntity;
import ru.yaal.project.articlesaver.resource.ResourceEntity;

import java.util.Arrays;

public class JpaArticleWriterTest {
    @BeforeClass
    public void logger() {
        BasicConfigurator.configure();
    }

    @Test
    public void save() throws Exception {
        ResourceEntity resource = new ResourceEntity();
        resource.setOriginalUrl("http://jquery.com/lib.js");
        resource.setFileName("lib.js");
        resource.setContent("<head></head>".getBytes());

        ArticleEntity article = new ArticleEntity();
        article.setName("My Test Article");
        article.setHtml("<html></html>");
        article.setUrl("http://yaal.ru");
        article.setResources(Arrays.asList(resource));

        IArticleWriter writer = new JpaArticleWriter();
        writer.save(article);
    }

}
