package searchengine.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(indexes = @Index(columnList = "Path", name = "path_index"))
public class Page
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Site site;
    //@Column(columnDefinition = "TEXT", nullable = false)
    @Column(length = 1000, nullable = false)
    private String path;
    @Column(nullable = false)
    private Integer code;
    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;
}
