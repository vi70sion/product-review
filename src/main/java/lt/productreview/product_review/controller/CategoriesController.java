package lt.productreview.product_review.controller;

import lt.productreview.product_review.service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    @Autowired
    CategorieService categorieService;

    @GetMapping("/all")
    public ResponseEntity<List<String>> allCategoriesList() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categorieService.allCategoriesList());
    }

}
