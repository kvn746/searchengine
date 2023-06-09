package searchengine.services;

import lombok.RequiredArgsConstructor;
import searchengine.config.ParserSettings;
import searchengine.dto.indexing.SitePage;
import searchengine.model.Site;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RecursiveTask;

@RequiredArgsConstructor
public class FindPageService extends RecursiveTask<SitePage> {
    private final Site site;
    private final SitePage sitePage;
    private final String DOMEN;
    private final ParserSettings parserSettings;

    @Override
    protected SitePage compute() {
        try {
            FutureTask<HashSet<SitePage>> task = new FutureTask<>(new GetChildLinks(site, sitePage, DOMEN, parserSettings));
            new Thread(task).start();
            sitePage.setChildren(task.get());

            List<FindPageService> taskList = new ArrayList<>();

            for (SitePage child : sitePage.getChildren()) {
                FindPageService childTask = new FindPageService(site, child, DOMEN, parserSettings);
                childTask.fork();
                taskList.add(childTask);
            }

            for (FindPageService childTask : taskList) {
                childTask.join();
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        return sitePage;
    }
}
