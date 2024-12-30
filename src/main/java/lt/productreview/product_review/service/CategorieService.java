package lt.productreview.product_review.service;

import lt.productreview.product_review.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieService {

    @Autowired
    CategoriesRepository categorieRepository;

    public List<String> allCategoriesList() {
        return categorieRepository.allCategoriesList();
    }


}
