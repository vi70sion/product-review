package lt.productreview.product_review.service;

import lt.productreview.product_review.model.EmailForm;
import lt.productreview.product_review.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EmailProcessor implements Runnable {

    @Autowired
    EmailRepository emailRepository;
    @Autowired
    EmailSender emailSender;
    ConcurrentHashMap<Integer, EmailForm> mailMap = new ConcurrentHashMap<>();

    @Override
    public void run() {
        while (true) {
            try {
                processEmails();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.currentThread().sleep(2 * 60 * 1000); // 2 min
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " thread interrupted.");
            }
        }
    }

    private synchronized void processEmails() throws IOException {
        EmailForm emailForm = emailRepository.getOneEmail();
        if (emailForm != null) {
            if (!mailMap.containsKey(emailForm.getId())) {
                //there is no such email yet in mailMap
                mailMap.putIfAbsent(emailForm.getId(),emailForm);
                System.out.println(Thread.currentThread().getName() + " sending email with ID: " + emailForm.getId());

                List<String> subscribersList = new ArrayList<>();
                subscribersList = emailRepository.getNewsSubscribersEmailsList();
                for (String email : subscribersList) {
                    emailSender.sendEmail(email, emailForm.getContent());
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " thread interrupted.");
                }
                emailRepository.updateEmail(emailForm);
                mailMap.remove(emailForm.getId());
            } else {
                System.out.println(Thread.currentThread().getName() + " Email with ID: " + emailForm.getId() + " already processing by another thread.");
            }
        }
    }

}
