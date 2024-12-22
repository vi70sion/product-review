package lt.productreview.product_review.model;

import java.util.UUID;

class AdminUser extends User {

    public AdminUser(UUID id, String name, String email, String password) {
        super(id, name, email, password, Role.ADMIN);
    }

//    @Override
//    public void performRole() {
//        System.out.println("Admin user can manage categories and users.");
//    }

}
