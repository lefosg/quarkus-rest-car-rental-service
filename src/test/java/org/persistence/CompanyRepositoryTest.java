package org.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.domain.Company;
import org.junit.jupiter.api.Test;
import org.util.IntegrationBase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CompanyRepositoryTest extends IntegrationBase {

    @Inject
    CompanyRepository companyRepository;

    @Test
    void findByCity() {
        List<Company> companies = companyRepository.findByCity("ΑΘΗΝΑ");
        assertEquals(1, companies.size());
    }

    @Test
    @Transactional
    void deleteAllCompanies() {
        companyRepository.deleteAllCompanies();
        assertEquals(0, companyRepository.listAll().size());
    }

    @Test
    @Transactional
    void deleteOneCompanyValid() {
        companyRepository.deleteCompany(2000);
        List<Company> companies = companyRepository.listAll();
        assertEquals(1, companies.size());
        assertNull(companyRepository.findById(2000));
    }

    @Test
    @Transactional
    void deleteOneCompanyInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
           companyRepository.deleteCompany(null);
        });

        assertThrows(NotFoundException.class, () -> {
            companyRepository.deleteCompany(2002);
        });
    }

    @Test
    void findNonExistingId() {
        assertNull(companyRepository.findById(2022));
    }
}