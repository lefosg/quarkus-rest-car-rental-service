package org.representation;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.domain.TechnicalCheck;
import org.domain.TechnicalCheckImpl;
import org.junit.jupiter.api.Test;
import org.persistence.TechnicalCheckRepository;
import org.util.DamageType;
import org.util.IntegrationBase;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TechnicalCheckMapperTest extends IntegrationBase {

    @Inject
    TechnicalCheckMapper technicalCheckMapper;

    @Inject
    TechnicalCheckRepository technicalCheckRepository;

    @Transactional
    @Test
    public void testToModel() {
        TechnicalCheckRepresentation representation = createTechnicalCheckRepresentation(5000);

        TechnicalCheck model = technicalCheckMapper.toModel(representation);
        assertNotNull(model);
        assertEquals(model.getId(), representation.id);
        assertEquals(model.getRent().getId(), representation.rent);
        assertEquals(((TechnicalCheckImpl)model).getDamageType(), representation.damageType);
    }

    @Transactional
    @Test
    public void testToRepresentation() {
        TechnicalCheck model = technicalCheckRepository.findById(5000);
        assertNotNull(model);

        TechnicalCheckRepresentation representation = technicalCheckMapper.toRepresentation(model);
        assertNotNull(representation);
        assertEquals(representation.id, model.getId());
        assertEquals(representation.rent, model.getRent().getId());
        assertEquals(representation.damageType, ((TechnicalCheckImpl)model).getDamageType());
    }


    private TechnicalCheckRepresentation createTechnicalCheckRepresentation(Integer id) {
        TechnicalCheckRepresentation representation = new TechnicalCheckRepresentation();
        representation.id = id;
        representation.damageType = DamageType.Glasses;
        representation.rent = 4000;
        return representation;
    }

}