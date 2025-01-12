package lt.productreview.product_review;

import lt.productreview.product_review.model.RegularUser;
import lt.productreview.product_review.repository.EmailRepository;
import lt.productreview.product_review.repository.UserDataRepository;
import lt.productreview.product_review.service.JwtUtil;
import lt.productreview.product_review.service.UserDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserDataServiceTest {

    @Mock
    private UserDataRepository userDataReposirory;

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserDataService userDataService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUserSuccess() {
        RegularUser user = new RegularUser(UUID.randomUUID(),"Name","test@example.com", "password123");

        when(userDataReposirory.emailExistValidation(user.getEmail())).thenReturn(false);
        when(userDataReposirory.addUser(any(RegularUser.class))).thenReturn(true);

        ResponseEntity<String> response = userDataService.addUser(user);

        verify(userDataReposirory).addUser(any(RegularUser.class));
        verify(emailRepository).saveNewsSubscriber(user.getEmail());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User added successfully.", response.getBody());
    }

    @Test
    void testHashPasswordWithSecret() {
        String password = "password123";
        String secretWord = "secret";

        String hashedPassword = userDataService.hashPasswordWithSecret(password, secretWord);

        // known hash value for consistency
        String expectedHash = "9fad3543a48308c8dff179b4c88444c66fae816d9b15587f9f30a3201571afe4";
        assertEquals(expectedHash, hashedPassword);
    }
}
