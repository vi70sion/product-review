package lt.productreview.product_review.model;

import java.time.LocalDateTime;

public class EmailForm {

    private int id;
    private String content;
    private LocalDateTime sent;

    public EmailForm(int id, String content, LocalDateTime sent) {
        this.id = id;
        this.content = content;
        this.sent = sent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSent() {
        return sent;
    }

    public void setSent(LocalDateTime sent) {
        this.sent = sent;
    }
}
