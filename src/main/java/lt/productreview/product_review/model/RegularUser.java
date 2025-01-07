package lt.productreview.product_review.model;

import java.util.UUID;

public class RegularUser extends User {

    public RegularUser(UUID id, String name, String email, String password) {
        super(id, name, email, password, Role.USER);
    }

    @Override
    public void performRole() {
        System.out.println("Regular user can write reviews.");
    }

}
