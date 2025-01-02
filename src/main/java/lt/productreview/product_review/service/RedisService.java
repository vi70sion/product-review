package lt.productreview.product_review.service;

import lt.productreview.product_review.controller.MessagingController;
import lt.productreview.product_review.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

@Service
public class RedisService {

    private final JedisPool jedisPool;
    @Autowired
    private UserDataService userDataService;

    public RedisService(String host, int port) {
        this.jedisPool = new JedisPool(host, port);
    }

    public String addReviewToRedis(Review review){
        String reviewString = userDataService.getUserNameById(review.getUserId())
                + " left a review: " + review.getProductName() + ", rating (" + review.getRating() + "/5), at "
                + review.getCreatedAt().withNano(0).format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a", Locale.US));
        put("last_review", reviewString);
        return reviewString;
    }

    public Set<String> getKeys() {
        Jedis jedis = jedisPool.getResource();
        Set<String> keys = jedis.keys("review_*");
        return keys;
    }

    public void put(String key, String numbersAsJson) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, numbersAsJson);
        }
    }

    public String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    public void close() {
        jedisPool.close();
    }
}
