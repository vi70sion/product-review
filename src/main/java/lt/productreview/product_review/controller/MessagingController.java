package lt.productreview.product_review.controller;

import lt.productreview.product_review.service.AuthorizationService;
import lt.productreview.product_review.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Controller
public class MessagingController {

    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private RedisService redisService;
    @Autowired
    public MessagingController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public String sendMessage(String message, SimpMessageHeaderAccessor headerAccessor) {
        String token = headerAccessor.getFirstNativeHeader("Authorization");
        ResponseEntity<String> validationResponse = authorizationService.validateAuthorizationHeader(token);
        if (validationResponse != null) {
            return "";
        }
        return message;
    }

    public void sendMessageToTopic(String message) {
        if(!message.startsWith("[Welcome]")) {
            messagingTemplate.convertAndSend("/topic/messages", message);
        }
    }

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String lastReview = redisService.get("last_review");
        // Check if the user is connecting to „/topic/messages“
        if ("/topic/messages".equals(headerAccessor.getDestination())) {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.schedule(() -> {
                messagingTemplate.convertAndSend("/topic/welcome", "[Welcome]" + lastReview);
            }, 1000, TimeUnit.MILLISECONDS);
        }
    }

}
