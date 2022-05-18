package reqres.perfomance;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class PerformanceTest {

    @Test
    public void performanceTest() throws InterruptedException {
        int threadNumber = 100;
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadNumber; i++) {
            UsersThread request = new UsersThread(i);
            Thread thread = new Thread(request);
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        Assert.assertEquals( 0, UsersThread.failuresCount, String.format("There is %s unsuccessful responses", UsersThread.failuresCount));
    }
}
