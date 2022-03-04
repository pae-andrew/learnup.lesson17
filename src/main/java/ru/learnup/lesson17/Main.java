package ru.learnup.lesson17;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        LOG.debug("Start out threads...");

        int[] massive = new int[1000];
        for (int i = 0; i < 1000; i++) {
            massive[i] = (100 + new Random().nextInt(100));
        }
        List<Integer> divisors = List.of(3, 5, 7, 9, 11);
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future<Integer>> futures = new ArrayList<>();
        for (int x : divisors) {
            Future<Integer> future = service.submit(() -> {
                int sum = 0;
                for (int a : massive) {
                    if (a % x == 0) {
                        sum += a;
                    }
                }
                return sum;
            });
            futures.add(future);
        }
        service.shutdown();
        LOG.debug("Threads finished. Lets find max sum.");

        int sum = 0;
        int i = 0;
        for (Future<Integer> future : futures) {
            int x = future.get();
            if (x > sum) {
                sum = x;
                i = futures.indexOf(future);
            }
        }

        System.out.println("Сумма чисел делящихся на " + divisors.get(i) + " максимальная и равна " + sum);

        LOG.debug("Program ended successfully.");
    }
}