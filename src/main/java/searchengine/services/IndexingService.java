package searchengine.services;

import searchengine.dto.indexing.IndexingResponse;
import searchengine.model.IndexingStatus;

public interface IndexingService
{
    IndexingResponse startIndexing();
}
