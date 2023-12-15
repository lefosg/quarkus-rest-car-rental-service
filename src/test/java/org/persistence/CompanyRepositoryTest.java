package org.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.domain.Company;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CompanyRepositoryTest {

    @Inject
    CompanyRepository companyRepository;

    @Test
    void findByCity() {
        List<Company> companies = companyRepository.findByCity("ΑΘΗΝΑ");
        assertEquals(1, companies.size());
    }
}