package lt.productreview.product_review.model;

class AdminUser extends User {

    public AdminUser(String name, String email, String password) {
        super(name, email, password, Role.ADMIN);
    }

    @Override
    public void performRole() {
        System.out.println("Admin user can manage categories and users.");
    }

}
