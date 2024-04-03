package org.infastructure.rest.representation;

import jakarta.inject.Inject;
import org.domain.Rents.RentRepository;
import org.domain.TechnicalCheck.TechnicalCheck;
import org.domain.TechnicalCheck.TechnicalCheckImpl;
import org.mapstruct.*;


import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = TechnicalCheckMapper.class)
public abstract class TechnicalCheckMapper {

    @Inject
    RentRepository rentRepository;

    @Mapping(source = "rent.id", target = "rent")
    public abstract TechnicalCheckRepresentation toRepresentation(TechnicalCheck technicalCheck);
    public abstract List<TechnicalCheckRepresentation> toRepresentationList(List<TechnicalCheck> technicalCheck);

    @Mapping(source = "rent.id", target = "rent")
    public abstract TechnicalCheckRepresentation toRepresentation(TechnicalCheckImpl technicalCheck);
    //public abstract List<TechnicalCheckRepresentation> toRepresentationList(List<TechnicalCheckImpl> technicalCheck);

    @BeanMapping(resultType = TechnicalCheckImpl.class)
    @Mapping(source = "damageType", target = "damageType")
    @Mapping(target = "rent", ignore = true)
    public abstract TechnicalCheck toModel(TechnicalCheckRepresentation representation);

    @AfterMapping
    protected void fillDomain(TechnicalCheckRepresentation representation, @MappingTarget TechnicalCheck technicalCheck) {
        TechnicalCheckImpl tc = (TechnicalCheckImpl) technicalCheck;
        tc.setDamageType(representation.damageType);
        tc.setRent(rentRepository.findRentById(representation.rent));
    }

    @AfterMapping
    protected void fillRepresentation(TechnicalCheck technicalCheck, @MappingTarget TechnicalCheckRepresentation representation) {
        if (technicalCheck instanceof TechnicalCheckImpl) {
            representation.damageType = ((TechnicalCheckImpl) technicalCheck).getDamageType();
        }
        //representation.rent = tc.getRent().getId();
    }
}
