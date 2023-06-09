package searchengine.model;

import org.springframework.data.repository.CrudRepository;

public interface SiteRepository extends CrudRepository<Site, Integer> {
    Object findFirstByUrl(String siteUrl);
}
