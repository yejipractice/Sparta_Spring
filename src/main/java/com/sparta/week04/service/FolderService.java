package com.sparta.week04.service;

import com.sparta.week04.models.Folder;
import com.sparta.week04.models.Product;
import com.sparta.week04.models.User;
import com.sparta.week04.repository.FolderRepository;
import com.sparta.week04.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FolderService {
    // 멤버 변수 선언
    private final FolderRepository folderRepository;
    private final ProductRepository productRepository;

    // 생성자: ProductFolderService() 가 생성될 때 호출됨
    @Autowired
    public FolderService(FolderRepository folderRepository, ProductRepository productRepository) {
        // 멤버 변수 생성
        this.folderRepository = folderRepository;
        this.productRepository = productRepository;
    }

    // 회원 ID 로 등록된 모든 폴더 조회
    public List<Folder> getFolders(User user) {
        return folderRepository.findAllByUser(user);
    }

    public List<Folder> createFolders(List<String> folderNameList, User user) {
        // 입력 들어온 폴더 이름들을 기준으로 조회
        List<Folder> existFolderList = folderRepository.findAllByUserAndNameIn(user, folderNameList);
        List<Folder> folderList = new ArrayList<>();
        for (String folderName : folderNameList) {
            if(!isExistFolderName(folderName, existFolderList)){
                Folder folder = new Folder(folderName, user);
                folderList.add(folder);
            }
        }
        // 빈 배열이면 알아서 동작하지 않음
        folderList = folderRepository.saveAll(folderList);
        return folderList;
    }

    // 기존 폴더 리스트에서 folder name 이 있는지 체크
    public boolean isExistFolderName(String folderName, List<Folder> existFolderList){
        for (Folder existFolder : existFolderList){
            if(existFolder.getName().equals(folderName)){
                return true;
            }
        }
        return false;
    }

    // 회원 ID가 소유한 폴더에 저장되어 있는 상품들 조회
    public Page<Product> getProductsOnFolder(User user, int page, int size, String sortBy, boolean isAsc, Long folderId){
        Sort.Direction direction = isAsc? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.findAllByUserIdAndFolderList_Id(user.getId(), folderId, pageable);
    }
}