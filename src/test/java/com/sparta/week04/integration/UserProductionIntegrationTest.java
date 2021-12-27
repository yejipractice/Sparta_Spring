package com.sparta.week04.integration;

import com.sparta.week04.dto.ProductMypriceRequestDto;
import com.sparta.week04.dto.ProductRequestDto;
import com.sparta.week04.dto.SignupRequestDto;
import com.sparta.week04.models.Product;
import com.sparta.week04.models.User;
import com.sparta.week04.models.UserRole;
import com.sparta.week04.repository.UserRepository;
import com.sparta.week04.service.ProductService;
import com.sparta.week04.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserProductionIntegrationTest {
    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    Long userId = null;
    Product createdProduct = null;
    String title = "Apple <b>에어팟</b> 2세대 유선충전 모델 (MV7N2KH/A)";
    String imageUrl = "https://shopping-phinf.pstatic.net/main_1862208/18622086330.20200831140839.jpg";
    String linkUrl = "https://search.shopping.naver.com/gate.nhn?id=18622086330";
    int lPrice = 77000;
    int updatedMyPrice = -1;
    @Test
    @Order(1)
    @DisplayName("회원가입 전 관심 상품 등록")
    void test1() {
        // given
        ProductRequestDto requestDto = new ProductRequestDto(
                title,
                linkUrl,
                imageUrl,
                lPrice
        );
        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(requestDto, userId);
        });
        // then
        assertEquals("회원 Id 가 유효하지 않습니다.", exception.getMessage());
    }

    @Test
    @Order(2)
    @DisplayName("회원가입")
    void test2() {
        // given
        String username = "yeji";
        String password = "yeji123";
        String email = "yeji@yeji.com";
        SignupRequestDto requestDto = new SignupRequestDto(username, password, email);

        //when
        User user = userService.registerUser(requestDto);

        // then
        assertNotNull(user.getId());
        assertEquals(username, user.getUsername());
        assertTrue(passwordEncoder.matches(password, user.getPassword()));
        assertEquals(email, user.getEmail());
        assertEquals(UserRole.USER, user.getRole());
        this.userId = user.getId();
    }

    @Test
    @Order(3)
    @DisplayName("가입된 회원으로 관심상품 등록")
    void test3() {
        // given
        ProductRequestDto requestDto = new ProductRequestDto(
                title,
                linkUrl,
                imageUrl,
                lPrice
        );
        //when
        Product product = productService.createProduct(requestDto, userId);
        // then
        assertNotNull(product.getId());
        assertEquals(userId, product.getUserId());
        assertEquals(title, product.getTitle());
        assertEquals(imageUrl, product.getImage());
        assertEquals(linkUrl, product.getLink());
        assertEquals(lPrice, product.getLprice());
        assertEquals(0, product.getMyprice());
        createdProduct = product;
    }

    @Test
    @Order(4)
    @DisplayName("관심상품 업데이트")
    void test4() {
        // given
        Long productId = this.createdProduct.getId();
        int myPrice = 70000;
        ProductMypriceRequestDto requestDto = new ProductMypriceRequestDto(myPrice);
        //when
        Product product = productService.updateProduct(productId, requestDto);
        // then
        assertNotNull(product.getId());
        assertEquals(userId, product.getUserId());
        assertEquals(this.createdProduct.getTitle(), product.getTitle());
        assertEquals(this.createdProduct.getImage(), product.getImage());
        assertEquals(this.createdProduct.getLink(), product.getLink());
        assertEquals(this.createdProduct.getLprice(), product.getLprice());
        assertEquals(myPrice, product.getMyprice());
        this.updatedMyPrice = product.getMyprice();
    }

    @Test
    @Order(5)
    @DisplayName("회원이 등록한 관심상품 조회")
    void test5() {
        // given
        int page = 0;
        int size = 10;
        String sortBy = "id";
        boolean isAsc = false;
        //then
        Page<Product> productList = productService.getProducts(userId, page, size, sortBy, isAsc);
        //then
        Long createdProductId = createdProduct.getId();
        Product foundProduct = productList.stream()
                .filter(product -> product.getId().equals(createdProductId))
                .findFirst()
                .orElse(null);
        assertNotNull(foundProduct);
        assertEquals(userId, foundProduct.getUserId());
        assertEquals(this.createdProduct.getId(), foundProduct.getId());
        assertEquals(this.createdProduct.getTitle(), foundProduct.getTitle());
        assertEquals(this.createdProduct.getImage(), foundProduct.getImage());
        assertEquals(this.createdProduct.getLink(), foundProduct.getLink());
        assertEquals(this.createdProduct.getLprice(), foundProduct.getLprice());
        assertEquals(this.updatedMyPrice, foundProduct.getMyprice());

    }

}
