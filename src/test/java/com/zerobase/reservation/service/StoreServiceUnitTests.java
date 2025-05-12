package com.zerobase.reservation.service;

import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.repository.PartnerRepository;
import com.zerobase.reservation.repository.StoreCategoryRepository;
import com.zerobase.reservation.repository.StoreRepository;
import com.zerobase.reservation.service.impl.StoreServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StoreServiceUnitTests {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private PartnerRepository partnerRepository;

    @Mock
    private StoreCategoryRepository storeCategoryRepository;

    @InjectMocks
    private StoreServiceImpl storeService;

    @Test
    void testUpdateStore(){
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
        when(storeRepository.save(ArgumentMatchers.<Store>any())).thenReturn(updated);

        Store result = storeService.updateStore(1L, updated);

        assertEquals("New Store", result.getName());
        assertEquals("New Address", result.getAddress());
    }

    // 매장 삭제 기능 Test

    @Test
    void testDeleteStore(){
        Store store = Store.builder().id(1L).build();

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

        assertDoesNotThrow(() -> storeService.deleteStore(1L));
        verify(storeRepository).delete(store);
    }
}
