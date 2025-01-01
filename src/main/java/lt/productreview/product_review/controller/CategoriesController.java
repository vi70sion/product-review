package lt.productreview.product_review.controller;

import lt.productreview.product_review.service.AuthorizationService;
import lt.productreview.product_review.service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    @Autowired
    CategorieService categorieService;
    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/all")
    public ResponseEntity<?> allCategoriesList(@RequestHeader("Authorization") String authorizationHeader) {
        ResponseEntity<String> validationResponse = authorizationService.validateAuthorizationHeader(authorizationHeader);
        if (validationResponse != null) {
            return validationResponse;
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categorieService.allCategoriesList());
    }



}
