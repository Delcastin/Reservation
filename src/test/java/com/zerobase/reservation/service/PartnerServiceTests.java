package com.zerobase.reservation.service;



import com.zerobase.reservation.domain.Partner;
import com.zerobase.reservation.repository.PartnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PartnerServiceTests {

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private PartnerRepository partnerRepository;

    @BeforeEach
    void setUp() {
        partnerRepository.deleteAll();
    }

    @Test
    void testRegisterPartner(){
        Partner partner = Partner.builder()
                .email("test@gmail.com")
                .name("Test")
                .password("123")
                .build();

        Partner savedPartner = partnerRepository.save(partner);

        assertNotNull(savedPartner.getId());
        assertEquals("test@gmail.com", savedPartner.getEmail());
    }

    @Test
    void testFindByEmail(){

        Partner partner = Partner.builder()
                .email("test@gmail.com")
                .name("Test")
                .password("123")
                .build();

        partnerService.registerPartner(partner);

        Optional<Partner> result = partnerService.findByEmail("test@gmail.com");
        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getName());
    }

    @Test
    void testFindByIdSuccess(){
        Partner partner = Partner.builder()
                .email("test@gmail.com")
                .name("Test")
                .password("123")
                .build();

        Partner saved = partnerService.registerPartner(partner);
        Partner found = partnerService.findById(saved.getId());

        assertNotNull(found);
        assertEquals("Test", found.getName());
    }

    @Test
    void testFindByIdFail(){
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> partnerService.findById(999L));

        assertEquals("해당 파트너를 찾을 수 없습니다.", exception.getMessage());
    }
}
