package lt.productreview.product_review.model;

class RegularUser extends User {

    public RegularUser(String name, String email, String password) {
        super(name, email, password, Role.USER);
    }

    @Override
    public void performRole() {
        System.out.println("Regular user can write reviews.");
    }

}
