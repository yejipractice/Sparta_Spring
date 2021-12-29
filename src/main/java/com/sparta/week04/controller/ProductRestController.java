package com.sparta.week04.controller;

import com.sparta.week04.models.Product;
import com.sparta.week04.dto.ProductMypriceRequestDto;
import com.sparta.week04.models.User;
import com.sparta.week04.repository.ProductRepository;
import com.sparta.week04.dto.ProductRequestDto;
import com.sparta.week04.security.UserDetailsImpl;
import com.sparta.week04.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor // final로 선언된 멤버 변수를 자동으로 생성합니다.
@RestController // JSON으로 데이터를 주고받음을 선언합니다.
public class ProductRestController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    // 등록된 전체 상품 목록 조회
    @GetMapping("/api/products")
    public Page<Product> getProducts(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUser().getId();
        page = page -1;
        return productService.getProducts(userId, page, size, sortBy, isAsc);
    }

    // 신규 상품 등록
    @PostMapping("/api/products")
    public Product createProduct(@RequestBody ProductRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();

        Product product = new Product(requestDto, userId);
        return product;
    }

    @PutMapping("/api/products")
    public Long updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto){
        return productService.update(id, requestDto);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/api/admin/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // 상품에 폴더 추가
    @PostMapping("/api/products/{id}/folder")
    public Long addFolder(@PathVariable Long id,
                          @RequestParam("folderId") Long folderId,
                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        Product product = productService.addFolder(id, folderId, user);
        return  product.getId();
    }
}
