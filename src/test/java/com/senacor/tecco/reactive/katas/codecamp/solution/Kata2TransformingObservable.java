package com.senacor.tecco.reactive.katas.codecamp.solution;

import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata2TransformingObservable {

    private final WikiService wikiService = new WikiService();

    @Test
    public void transformingObservable() throws Exception {
        // 1. Benutze den WikiService (fetchArticleObservable) und hole dir einen beliebigen Wikipedia Artikel
        // 2. Transformiere das Ergebnis mit Hilfe von WikiService#parseMediaWikiTextObservable in eine Objektstruktur
        // 3. gib den Wikipedia Artikel Text in der Console aus (ParsedPage.getText())

        wikiService.fetchArticleObservable("Bilbilis")
                .doOnNext(debug -> print("fetchArticleObservable res: %s", debug))
                .flatMap(wikiService::parseMediaWikiTextObservable)
                .doOnNext(debug -> print("parseMediaWikiTextObservable res: %s", debug))
                .subscribe(next -> print("next: %s", next.getText()),
                        Throwable::printStackTrace,
                        () -> print("complete!"));

    }


}
