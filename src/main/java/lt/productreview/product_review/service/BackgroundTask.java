package lt.productreview.product_review.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BackgroundTask {

    @Autowired
    private EmailProcessor emailProcessor;

    @PostConstruct
    public void performEmailSent() {

        Thread emailThread = new Thread(emailProcessor);
        emailThread.start();
        System.out.println("Email processing thread started.");



    }
}
