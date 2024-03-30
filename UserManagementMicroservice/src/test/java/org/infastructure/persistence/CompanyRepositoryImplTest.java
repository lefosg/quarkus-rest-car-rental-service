package org.infastructure.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
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

    //todo? replace impl with interface (qualifier?)
    @Inject
    CompanyRepositoryImpl companyRepository;

    @Test
    void findByCity() {
        List<Company> companies = companyRepository.findByCity("ΑΘΗΝΑ");
        assertEquals(1, companies.size());
    }

    @Test
    void findNonExistingId() {
        assertNull(companyRepository.findById(2022));
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

}