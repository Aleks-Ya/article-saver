package ru.yaal.project.articlesaver.resource;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.IOException;

/**
 * Ресурс, на который ссылается html-страница со статьей (изображение, CSS, JavaScript).
 * User: Aleks
 * Date: 03.09.13
 * Time: 7:09
 */
@Entity
public class ResourceEntity extends AbstractResource {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @SuppressWarnings("unused")
    private long id;
    @Lob
    private byte[] content;
    private String originalUrl;
    private String fileName;

    public ResourceEntity() {
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    @Override
    public byte[] getContent() throws IOException {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}
