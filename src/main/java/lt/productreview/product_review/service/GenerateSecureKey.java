package lt.productreview.product_review.service;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

public class GenerateSecureKey {


    Key secureKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    String base64Key = Encoders.BASE64.encode(secureKey.getEncoded());


}
