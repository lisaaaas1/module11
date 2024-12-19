package org.example.task02;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PoolExample {
    public static void main(String[] args) throws InterruptedException {

        // создаем пул для выполнения наших задач
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                3, 3, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(3));

        // сколько задач выполнилось
        AtomicInteger count = new AtomicInteger(0);

        // сколько задач выполняется
        AtomicInteger inProgress = new AtomicInteger(0);

        // отправляем задачи на выполнение
        for (int i = 0; i < 30; i++) {
            final int number = i;
            Thread.sleep(10);

            //проверяем, есть ли свободные места в пуле, если нет, то останавливаем поток на 100мс
            while (executor.getQueue().remainingCapacity() == 0) {
                Thread.sleep(100);
            }

            System.out.println("creating #" + number);
            executor.submit(() -> {
                int working = inProgress.incrementAndGet();
                System.out.println("start #" + number + ", in progress: " + working);
                try {
                    // тут какая-то полезная работа
                    Thread.sleep(Math.round(1000 + Math.random() * 2000));
                } catch (InterruptedException e) {
                    // ignore
                }
                working = inProgress.decrementAndGet();
                System.out.println("end #" + number + ", in progress: " + working + ", done tasks: " + count.incrementAndGet());
                return null;
            });
        }

        //в теории может работать и без них, но никакой гарантии выполнения всех операций мы не получим
        executor.shutdown(); //гарант того, что мы не будем принимать новые задачи
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS); //ожидание завершения очень большое, чтобы гарантировать выполнение всех задач перед выходом
    }
}
