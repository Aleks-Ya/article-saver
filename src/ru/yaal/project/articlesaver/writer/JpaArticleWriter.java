package ru.yaal.project.articlesaver.writer;

import ru.yaal.project.articlesaver.article.IArticle;
import ru.yaal.project.articlesaver.resource.IResource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Сохраняет статью в БД.
 * User: a.yablokov
 * Date: 08.11.13
 */
public class JpaArticleWriter implements IArticleWriter {
    private final EntityManager em;

    public JpaArticleWriter() {
        Map<String, String> props = new HashMap<>();
        props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        props.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/article_saver");
        props.put("hibernate.connection.username", "postgres");
        props.put("hibernate.connection.password", "postgres");
        props.put("hibernate.connection.pool_size", "1");
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL82Dialect");
        props.put("hibernate.cache.provider_class", "org.hibernate.cache.internal.NoCachingRegionFactory");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.connection.autocommit", "true");

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("manager1", props);
        em = factory.createEntityManager();
    }

    @Override
    public void save(IArticle article) throws ArticleWriteException {
        try {
            em.getTransaction().begin();
            persistResources(article);
            em.persist(article);
            em.getTransaction().commit();
        } catch (IOException e) {
            throw new ArticleWriteException(e.getMessage(), e);
        }
    }

    private void persistResources(IArticle article) throws IOException {
        for (IResource resource : article.getResources()) {
            em.persist(resource);
        }
    }
}
