package org.infastructure.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.domain.company.Company;
import org.domain.company.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.util.IntegrationBase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CompanyRepositoryImplTest extends IntegrationBase {

    @Inject
    CompanyRepository companyRepository;

    @Test
    void findByCity() {
        List<Company> companies = companyRepository.findByCity("ΑΘΗΝΑ");
        assertEquals(1, companies.size());
    }

    @Test
    void findNonExistingId() {
        assertNull(companyRepository.findCompanyById(2022));
    }

    @Test
    @Transactional
    void deleteAllCompanies() {
        companyRepository.deleteAllCompanies();
        assertEquals(0, companyRepository.listAllCompanies().size());
    }

    @Test
    @Transactional
    void deleteOneCompanyValid() {
        companyRepository.deleteCompany(2000);
        List<Company> companies = companyRepository.listAllCompanies();
        assertEquals(1, companies.size());
        assertNull(companyRepository.findCompanyById(2000));
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
    @Transactional
    void findByEmailTest() {
        assertEquals(companyRepository.findByEmail("speed@gmail.com").get(0).getId(),2001);
    }

    @Test
    @Transactional
    void findInvalidEmailTest() {
        assertEquals(2,companyRepository.findByEmail(null).size());
    }

    @Test
    @Transactional
    void findByPhoneTest() {
        assertEquals(companyRepository.findByPhone("2644125415").get(0).getId(),2001);
    }

    @Test
    @Transactional
    void findInvalidPhoneTest() {
        assertEquals(2,companyRepository.findByPhone(null).size());
    }

    @Test
    @Transactional
    void findByAFMTest() {
        assertEquals(companyRepository.findByAFM("999641227").get(0).getId(),2001);
    }

    @Test
    @Transactional
    void findInvalidAFMTest() {
        assertEquals(2,companyRepository.findByAFM(null).size());
    }

    @Test
    @Transactional
    void findByNameTest() {
        assertEquals(companyRepository.findByName("SPEED").get(0).getId(),2001);
    }

    @Test
    @Transactional
    void findInvalidNameTest() {
        assertEquals(2,companyRepository.findByName(null).size());
    }
}