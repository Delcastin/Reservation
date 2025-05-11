package com.zerobase.reservation;

import com.zerobase.reservation.domain.Partner;
import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.repository.PartnerRepository;
import com.zerobase.reservation.repository.StoreCategoryRepository;
import com.zerobase.reservation.repository.StoreRepository;
import com.zerobase.reservation.service.impl.StoreServiceImpl;
import com.zerobase.reservation.store.StoreCategory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTests {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private PartnerRepository partnerRepository;

    @Mock
    private StoreCategoryRepository storeCategoryRepository;

    @InjectMocks
    private StoreServiceImpl storeService;

    private StoreCategory category;
    private Partner partner;

    @BeforeEach
    void setUp() {
        category = StoreCategory.builder().id(1L).name("카페").build();
        partner = Partner.builder().id(1L).email("test@gmail.com").name("정수이").build();
    }

    @Test
    void testRegisterStore() {
        Store store = Store.builder()
                .name("Compose카페")
                .storeCategory(category)
                .address("인천시 남동구 논현동 가로우")
                .partner(partner)
                .build();

        when(storeRepository.save(ArgumentMatchers.any(Store.class))).thenReturn(store);

        Store saved = storeService.registerStore(store);

        assertNotNull(saved);
        assertEquals("Compose카페", saved.getName());
        assertEquals(category, saved.getStoreCategory());
        assertEquals(partner, saved.getPartner());
    }

    @Test
    void testGetAllStores() {
        Store store1 = Store.builder().name("Store 1").storeCategory(category).partner(partner).address("서울").build();
        Store store2 = Store.builder().name("Store 2").storeCategory(category).partner(partner).address("부산").build();

        when(storeRepository.findAll()).thenReturn(List.of(store1, store2));

        List<Store> result = storeService.getAllStores();

        assertEquals(2, result.size());
    }

    @Test
    void testGetStoresByCategory() {
        Store store = Store.builder().name("BBQ").storeCategory(category).partner(partner).address("서울").build();

        when(storeRepository.findByStoreCategory_Id(1L)).thenReturn(List.of(store));

        List<Store> result = storeService.getStoresByCategory(1L);

        assertEquals(1, result.size());
        assertEquals("BBQ", result.get(0).getName());
    }

    @Test
    void testGetStoresByPartner() {
        Store store = Store.builder().name("GS25").storeCategory(category).partner(partner).address("편의점 거리").build();

        when(storeRepository.findByPartner_Id(1L)).thenReturn(List.of(store));

        List<Store> result = storeService.getStoresByPartner(1L);

        assertEquals(1, result.size());
        assertEquals("GS25", result.get(0).getName());
    }

    @Test
    void testFindStoreCategoryByName() {
        when(storeCategoryRepository.findByName("분식")).thenReturn(Optional.of(category));

        Optional<StoreCategory> result = storeService.findByName("분식");

        assertTrue(result.isPresent());
        assertEquals("카페", result.get().getName()); // 실제 이름이 "카페"였으므로
    }

    @Test
    void testUpdateStore() {
        Store existing = Store.builder()
                .id(1L)
                .name("Old Store")
                .address("Old Address")
                .build();

        Store updated = Store.builder()
                .name("New Store")
                .address("New Address")
                .build();

        when(storeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(storeRepository.save(ArgumentMatchers.any())).thenReturn(updated);

        Store result = storeService.updateStore(1L, updated);

        assertEquals("New Store", result.getName());
        assertEquals("New Address", result.getAddress());
    }

    @Test
    void testDeleteStore() {
        Store store = Store.builder().id(1L).build();

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

        assertDoesNotThrow(() -> storeService.deleteStore(1L));
        verify(storeRepository).delete(store);
    }
}
