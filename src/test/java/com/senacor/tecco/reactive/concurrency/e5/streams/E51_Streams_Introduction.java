package com.senacor.tecco.reactive.concurrency.e5.streams;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.Watch;
import com.senacor.tecco.reactive.concurrency.Summary;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Retrieves and combines plane information with streams
 *
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
public class E51_Streams_Introduction {
    private final WikiService wikiService = new WikiService("en");

    @Rule
    public final Watch watch = new Watch();

    @Test
    public void thatNumbersAreMultiplied() throws Exception {

        int[] numbers = {1, 2, 3};

        Arrays.stream(numbers)
                .map(number -> number *10)
                .forEach(number -> {
                    System.out.printf("Item %d \n",number);
                });
    }

    @Test
    public void thatResultIsCollected() throws Exception {

        String[] numbers = {"1", "2", "3"};

        List<String> resultList = Arrays.stream(numbers)
                .map(number -> "Item" + number)
                .collect(Collectors.<String>toList());

        resultList.forEach(System.out::println);
    }

}
