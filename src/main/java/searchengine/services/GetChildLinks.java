package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import searchengine.config.ParserSettings;
import searchengine.dto.indexing.SitePage;
import searchengine.model.Site;

import java.util.HashSet;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class GetChildLinks implements Callable
{
    private final Site site;
    private final SitePage sitePage;
    private final String DOMEN;
    private final ParserSettings parserSettings;
    private HashSet<SitePage> childs = new HashSet<>();

    @Override
    public HashSet<SitePage> call() throws Exception {
        Thread.sleep(200);
        Document page = getHtmlData(sitePage);

        if(page != null) {
            Elements elements = page.select("a");
            elements.forEach(element -> {
                String link = element.getAllElements().attr("href");
                if(!link.isBlank() && link.charAt(0) == '/') {
                    link = DOMEN + link;
                }
                if (
                    !link.isBlank() &&
                    link.matches("^[^" + parserSettings.getGetParameters() + "]+$") &&
                    !link.equals(sitePage.getUrl()) &&
                    !link.equals(sitePage.getUrl() + "/") &&
                    link.contains(sitePage.getUrl())
                ) {
                    SitePage result = new SitePage(link);
                    childs.add(result);
                }
            });
        }

        return childs;
    }

    private Document getHtmlData(SitePage sitePage)
    {
        try {
            Connection connection = Jsoup.connect(sitePage.getUrl()).
                    userAgent(parserSettings.getUserAgent()).
                    referrer(parserSettings.getReferrer());
            Document document = connection.get();
            sitePage.setContent(document);
            sitePage.setCode(connection.response().statusCode());
            sitePage.setSite(site);

            return document;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
