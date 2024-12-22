package lt.productreview.product_review.service;

import lt.productreview.product_review.model.User;
import lt.productreview.product_review.repository.UserDataReposirory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDataService {

    @Autowired
    UserDataReposirory userDataReposirory;

    public UUID validateUser(User user) {
        return userDataReposirory.validateUser(user);
    }

}
