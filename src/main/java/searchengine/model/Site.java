package searchengine.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Site
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum('INDEXING', 'INDEXED', 'FAILED')", nullable = false)
    private IndexingStatus status;
    @Column(name = "status_time", nullable = false)
    private LocalDateTime statusTime;
    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public IndexingStatus getStatus() {
        return status;
    }

    public void setStatus(IndexingStatus status) {
        this.status = status;
    }

    public LocalDateTime getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(LocalDateTime statusTime) {
        this.statusTime = statusTime;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
