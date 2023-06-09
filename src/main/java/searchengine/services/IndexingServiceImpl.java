package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.config.Error;
import searchengine.config.ErrorsList;
import searchengine.config.ParserSettings;
import searchengine.config.SitesList;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.dto.indexing.SitePage;
import searchengine.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@Service
@RequiredArgsConstructor
public class IndexingServiceImpl implements IndexingService
{
    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final SitesList sites;
    private final ErrorsList errors;
    private final ParserSettings parserSettings;

    @Override
    public IndexingResponse startIndexing() {

        List<searchengine.config.Site> sitesList = sites.getSites();
        IndexingResponse indexingResponse = new IndexingResponse();

        Iterable<Site> sites = siteRepository.findAll();
        for (Site site : sites) {
            if (site.getStatus().equals(IndexingStatus.INDEXING)) {
                indexingResponse.setResult(false);
                indexingResponse.setError(getErrorMessage("indexing_in_progress"));

                return indexingResponse;
            }
        }

        for (int i = 0; i < sitesList.size(); i++) {
            searchengine.config.Site siteFromConfig = sitesList.get(i);
            Site site = (Site) siteRepository.findFirstByUrl(siteFromConfig.getUrl());
            if (site != null) {
                siteRepository.delete(site);
            } else {
                site = new Site();
            }
            site.setUrl(siteFromConfig.getUrl());
            site.setName(siteFromConfig.getName());
            site.setStatus(IndexingStatus.INDEXING);
            site.setStatusTime(LocalDateTime.now());

            siteRepository.save(site);
            site = (Site) siteRepository.findFirstByUrl(site.getUrl());

            SitePage sitePage = findSitePages(site);
            if (sitePage != null) {
                savePages(sitePage);
                site.setStatus(IndexingStatus.INDEXED);
                site.setStatusTime(LocalDateTime.now());
                siteRepository. save(site);
            }
        }

        indexingResponse.setResult(true);

        return indexingResponse;
    }

    public SitePage findSitePages(Site site)
    {
        String domen = site.getUrl();
        SitePage sitePage = new SitePage(domen);
        try {
            sitePage = new ForkJoinPool().invoke(new FindPageService(site, sitePage, domen, parserSettings));
        }
        catch (Exception ex) {
            ex.printStackTrace();

            return null;
        }

        return sitePage;
    }

    private void savePages(SitePage sitePage)
    {
        if (pageRepository.findFirstByPath(sitePage.getUrl()) == null) {
            Page page = new Page();
            page.setSite(sitePage.getSite());
            page.setPath(sitePage.getUrl());
            page.setCode(sitePage.getCode());
            page.setContent(sitePage.getContent().text());
            pageRepository.save(page);
        }
        for (SitePage child : sitePage.getChildren()) {
            savePages(child);
        }
    }

    public String getErrorMessage(String name) {
        List<Error> errorsList = errors.getErrors();
        String errorMessage = null;
        for (Error error : errorsList) {
            if (error.getName().equals(name)) {
                errorMessage = error.getMessage();
            }
        }

        return errorMessage;
    }

//    private Page getSitePage(Site site)
//    {
//        Page page = new Page();
//        try {
//            Connection connection = Jsoup.connect(site.getUrl()).
//                    userAgent(parserSettings.getUserAgent()).
//                    referrer(parserSettings.getReferrer());
//            Document html = connection.get();
//            page.setContent(html.data());
//            page.setCode(connection.response().statusCode());
//            page.setPath(site.getUrl());
//            page.setSite(site);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return page;
//    }
}
