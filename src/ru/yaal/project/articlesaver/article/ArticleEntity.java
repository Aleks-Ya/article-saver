package ru.yaal.project.articlesaver.article;

import org.hibernate.annotations.GenericGenerator;
import ru.yaal.project.articlesaver.resource.IResource;
import ru.yaal.project.articlesaver.resource.ResourceEntity;
import ru.yaal.project.articlesaver.url.UrlWrapper;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Статья для сохранения с помощью JPA.
 * User: a.yablokov
 * Date: 08.11.13
 */
@Entity
public class ArticleEntity implements IArticle {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @SuppressWarnings("unused")
    private long id;
    private String name;
    private String html;
    @OneToMany
    private List<ResourceEntity> resources;
    private String url;

    @Override
    public String getName() throws IOException {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<? extends IResource> getResources() throws IOException {
        return resources;
    }

    public void setResources(List<ResourceEntity> resources) {
        this.resources = resources;
    }

    @Override
    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    @Override
    public UrlWrapper getUrl() throws MalformedURLException {
        return new UrlWrapper(url);
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
