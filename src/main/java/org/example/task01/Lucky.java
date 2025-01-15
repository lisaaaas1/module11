package org.example.task01;

import java.util.concurrent.atomic.AtomicInteger;

public class Lucky {
    static AtomicInteger x = new AtomicInteger(0);
    static AtomicInteger count = new AtomicInteger(0);

    // определение класса потока LuckyThread (он определяет поведение потока)
    static class LuckyThread extends Thread {
        // переопределяем метод run(), чтобы указать, что должен делать поток, когда он будет запущен
        @Override
        public void run() {
            // проверяем, что потоки работают параллельно, отслеживаем выполнение
            System.out.println("Thread " + getName() + " started");
            while (x.get() < 999999) { // используем x.get() чисто для чтения значения
                int currentX = x.incrementAndGet();
                if ((currentX % 10) + (currentX / 10) % 10 + (currentX / 100) % 10 ==
                        (currentX / 1000) % 10 + (currentX / 10000) % 10 + (currentX / 100000) % 10) {
                    System.out.println("Thread " + getName() + " found lucky number: " + currentX);
                    count.incrementAndGet();
                }
            }
            System.out.println("Thread " + getName() + " finished");
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new LuckyThread();
        Thread t2 = new LuckyThread();
        Thread t3 = new LuckyThread();
        // инициируем создание новых потоков
        t1.start();
        t2.start();
        t3.start();
        // основной поток ждет, пока все потоки завершат свою работу
        t1.join();
        t2.join();
        t3.join();
        System.out.println("Total: " + count.get());
    }
}