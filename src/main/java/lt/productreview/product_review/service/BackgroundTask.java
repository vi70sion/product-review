package lt.productreview.product_review.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BackgroundTask {
    @Scheduled(fixedRate = 60*60*1000) // every hour
    public void performMemoryCleanup() {
        System.gc();
        System.out.println("Memory cleanup task completed.");
    }
}
