package com.senacor.tecco.reactive.util;

import com.senacor.tecco.reactive.services.integration.MediaWikiTextParser;
import com.senacor.tecco.reactive.services.integration.WikipediaServiceJapi;
import com.senacor.tecco.reactive.services.integration.WikipediaServiceJapiMock;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Andreas Keefer
 */
public class FlakyProxyTest {

    private final ParsedPage parsedPage = new MediaWikiTextParser().parse(
            "{{Dieser Artikel|behandelt das Jahr 42, weitere Bedeutungen finden sich unter [[42 (Begriffsklärung)]].}}\n" +
                    "== Religion ==\n" +
                    "\n" +
                    "* Der [[Evangelist (Neues Testament)|Evangelist]] [[Markus (Evangelist)|Markus]] gründet laut kirchlicher Tradition den [[Patriarch von Alexandrien|Bischofssitz]] in [[Alexandria]]. \n" +
                    "== Weblinks ==\n" +
                    "\n" +
                    "{{commonscat|42}}");

    @Test
    public void newJdkProxy() throws Exception {
        WikipediaServiceJapiMock target = new WikipediaServiceJapiMock(1);
        WikipediaServiceJapi wikipediaServiceJapi = FlakyProxy.newJdkProxy(target, FlakinessFunction.noFlakiness());
        String article = wikipediaServiceJapi.getArticle("42");
        assertThat(article, startsWith("{{Dieser Artikel|behandelt das Jahr 42"));
        assertThat(wikipediaServiceJapi, not(instanceOf(target.getClass())));
    }

    @Test(expected = UncheckedIOException.class)
    public void alwaysFail() throws Exception {
        DummyService target = new DummyServiceImpl();
        DummyService dummyService = FlakyProxy.newJdkProxy(target, FlakinessFunction.alwaysFail());
        dummyService.countWords(parsedPage);
    }

    @Test
    public void failCountDown() throws Exception {
        DummyService target = new DummyServiceImpl();
        DummyService dummyService = FlakyProxy.newJdkProxy(target, FlakinessFunction.failCountDown(3));
        dummyService.countWords(parsedPage);
        dummyService.countWords(parsedPage);
        try {
            dummyService.countWords(parsedPage);
            fail("UncheckedIOException expected");
        } catch (UncheckedIOException e) {
            assertThat(e.getCause(), instanceOf(IOException.class));
        }
    }
}