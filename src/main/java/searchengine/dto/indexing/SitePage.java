package searchengine.dto.indexing;

import lombok.Data;
import org.jsoup.nodes.Document;
import searchengine.model.Site;

import java.util.HashSet;

@Data
public class SitePage
{
    private final String url;
    private Site site;
    private Integer code;
    private Document content;
    private HashSet<SitePage> children = new HashSet<>();
}
