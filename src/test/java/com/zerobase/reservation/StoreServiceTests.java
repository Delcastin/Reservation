package com.zerobase.reservation;



import com.zerobase.reservation.domain.Partner;
import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.repository.PartnerRepository;
import com.zerobase.reservation.repository.StoreCategoryRepository;

import com.zerobase.reservation.repository.StoreRepository;
import com.zerobase.reservation.service.StoreService;
import com.zerobase.reservation.store.StoreCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class StoreServiceTests {

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private StoreCategoryRepository storeCategoryRepository;

    @BeforeEach
    void setUp() {
        storeRepository.deleteAll();
        storeCategoryRepository.deleteAll();
        partnerRepository.deleteAll();
    }

    @Test
    void testRegisterStore(){
        StoreCategory category = storeCategoryRepository.save(StoreCategory.builder()
                .name("카페")
                .build());

        Partner partner = partnerRepository.save(Partner.builder()
                .email("test@gmail.com")
                .name("정수이")
                .build());

        Store store = Store.builder()
                .name("Compose카페")
                .storeCategory(category)
                .address("인천시 남동구 논현동 가로우")
                .partner(partner)
                .build();

        Store saved = storeService.registerStore(store);

        assertNotNull(saved.getId());
        assertEquals("Compose카페", saved.getName());
        assertEquals(category.getId(), saved.getStoreCategory().getId());
        assertEquals(partner.getId(), saved.getPartner().getId());
    }

    @Test
    void testGetAllStores() {
        // StoreCategory 저장
        StoreCategory category = storeCategoryRepository.save(StoreCategory.builder()
                .name("중국집")
                .build());

        // Partner 저장
        Partner partner = partnerRepository.save(Partner.builder()
                .email("test@gmail.com")
                .name("Thomas")
                .password("123456")
                .build());

        // Store 저장
        Store store1 = storeRepository.save(Store.builder()
                .name("Store 1")
                .storeCategory(category)
                .partner(partner)
                .address("서울시 중구")
                .build());

        Store store2 = storeRepository.save(Store.builder()
                .name("Store 2")
                .storeCategory(category)
                .partner(partner)
                .address("서울시 강남구")
                .build());

        // 모든 Store 조회
        List<Store> stores = storeService.getAllStores();

        // 검증: Store가 2개 있어야 함
        assertEquals(2, stores.size());

        // 모든 StoreCategory 조회
        List<StoreCategory> categories = storeCategoryRepository.findAll();

        // 검증: StoreCategory가 1개 있어야 함 (중국집 카테고리)
        assertEquals(1, categories.size());

        // 정리: 데이터 삭제
        storeRepository.deleteAll();
        storeCategoryRepository.deleteAll();
        partnerRepository.deleteAll();
    }


    @Test
    void testGetStoresByCategory(){

        StoreCategory category1 = storeCategoryRepository.save(StoreCategory.builder()
                        .name("치킨집")
                        .build());

        StoreCategory category2 = storeCategoryRepository.save(StoreCategory.builder()
                        .name("중국집")
                        .build());

        Partner partner = partnerRepository.save(Partner.builder()
                        .email("test@gmail.com")
                        .name("Thomas")
                        .password("123456")
                        .build());

        storeService.registerStore(Store.builder()
                        .name("BBQ")
                        .storeCategory(category1)
                        .address("서울시 강남구 논현로")
                        .partner(partner)
                        .build());

        storeService.registerStore(Store.builder()
                        .name("Twosome Coffee")
                        .storeCategory(category2)
                        .partner(partner)
                        .address("서울시 강북구 동현로")
                        .build());

        List<Store> chickenStores = storeService.getStoresByCategory(category1.getId());
        assertEquals(1, chickenStores.size());
        assertEquals("BBQ", chickenStores.get(0).getName());
    }

    @Test
    void testGetStoresByPartner(){

        StoreCategory category = storeCategoryRepository.save(StoreCategory.builder()
                        .name("편의점")
                        .build());

        Partner partner1 = partnerRepository.save(Partner.builder()
                        .email("test1@gmail.com")
                        .name("Thomas")
                        .password("123456")
                        .build());

        Partner partner2 = partnerRepository.save(Partner.builder()
                        .email("test2@gmail.com")
                        .name("John")
                        .password("323232")
                        .build());

        storeService.registerStore(Store.builder()
                        .name("GS25")
                        .storeCategory(category)
                        .address("에스닷몰")
                        .partner(partner1)
                        .build());

        storeService.registerStore(Store.builder()
                        .name("CU")
                        .storeCategory(category)
                        .address("빅뱅건물")
                        .partner(partner2)
                        .build());

        List<Store> partnerStores = storeService.getStoresByPartner(partner1.getId());
        assertEquals(1, partnerStores.size());
        assertEquals("GS25", partnerStores.get(0).getName());
    }

    @Test
    void testFindStoreCategoryByName(){
        StoreCategory category = storeCategoryRepository.save(StoreCategory.builder()
                        .name("분식")
                        .build());

        Optional<StoreCategory> found = storeService.findByName("분식");

        assertTrue(found.isPresent());
        assertEquals("분식", found.get().getName());
    }
}
