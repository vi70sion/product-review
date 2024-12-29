package lt.productreview.product_review.service;

import lt.productreview.product_review.repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategorieService {

    @Autowired
    CategorieRepository categorieRepository;

    public List<String> allCategoriesList() {
        return categorieRepository.allCategoriesList();
    }


}
