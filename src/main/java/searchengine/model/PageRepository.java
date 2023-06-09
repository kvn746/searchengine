package searchengine.model;

import org.springframework.data.repository.CrudRepository;

public interface PageRepository extends CrudRepository<Page, Integer>
{
    Object findFirstByPath(String pagePath);
}
