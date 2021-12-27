package com.sparta.week04.testdata;

import com.sparta.week04.dto.ItemDto;
import com.sparta.week04.models.Product;
import com.sparta.week04.models.User;
import com.sparta.week04.models.UserRole;
import com.sparta.week04.repository.ProductRepository;
import com.sparta.week04.repository.UserRepository;
import com.sparta.week04.service.UserService;
import com.sparta.week04.utils.NaverShopSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component // 스프링 동작 시작 시 자동 실행
public class TestDataRunner implements ApplicationRunner {

    @Autowired
    UserService userService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    NaverShopSearch naverShopSearch;

    private static final int MIN_PRICE = 100;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 테스트 user 생성
        User testUser = new User("예디", passwordEncoder.encode("123"), "yeji@yeji.com", UserRole.USER);
        testUser = userRepository.save(testUser);

    }

    private void createTestData(User user, String searchWord){
        String jsonString = naverShopSearch.search(searchWord);
        List<ItemDto> itemDtoList = naverShopSearch.fromJSONtoItems(jsonString);
        List<Product> productList = new ArrayList<>();

        for(ItemDto itemDto : itemDtoList){
            Product product = new Product();
            product.setUserId(user.getId());
            product.setTitle(itemDto.getTitle());
            product.setLink(itemDto.getLink());
            product.setImage(itemDto.getImage());
            product.setLprice(itemDto.getLprice());

            int myPrice = getRandomNumber(MIN_PRICE, itemDto.getLprice()+10000);
            product.setMyprice(myPrice);
            productList.add(product);
        }
        productRepository.saveAll(productList);
    }

    public int getRandomNumber(int min, int max){
        return (int) ((Math.random()*(max-min))+min);
    }
}
