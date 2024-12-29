package lt.productreview.product_review.controller;

import lt.productreview.product_review.service.CategorieService;
import lt.productreview.product_review.service.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    @Autowired
    CategorieService categorieService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/all")
    public ResponseEntity<?> allCategoriesList(@RequestHeader("Authorization") String authorizationHeader) {
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Authorization header must be provided and start with 'Bearer '.");
        }
        String token = authorizationHeader.substring(7);
        if(!jwtUtil.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired JWT token.");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categorieService.allCategoriesList());
    }



}
