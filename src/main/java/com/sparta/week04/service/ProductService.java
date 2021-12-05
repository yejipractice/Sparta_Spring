package com.sparta.week04.service;

import com.sparta.week04.dto.ItemDto;
import com.sparta.week04.dto.ProductRequestDto;
import com.sparta.week04.models.Product;
import com.sparta.week04.dto.ProductMypriceRequestDto;
import com.sparta.week04.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor // final로 선언된 멤버 변수를 자동으로 생성합니다.
@Service // 서비스임을 선언합니다.
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional // 메소드 동작이 SQL 쿼리문임을 선언합니다.
    public Long update(Long id, ProductMypriceRequestDto requestDto) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
        );
        product.update(requestDto);
        return id;
    }

    @Transactional // 메소드 동작이 SQL 쿼리문임을 선언합니다.
    public Long updateBySearch(Long id, ItemDto itemDto) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
        );
        product.updateByItemDto(itemDto);
        return id;
    }

    @Transactional
    public Product createProduct(ProductRequestDto requestDto, Long userId) {
        Product product = new Product(requestDto, userId);
        productRepository.save(product);
        return product;
    }

    public List<Product> getProducts(Long userId) {
        return productRepository.findAllByUserId(userId);
    }
}