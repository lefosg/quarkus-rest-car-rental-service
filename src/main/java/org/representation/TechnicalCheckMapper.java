package org.representation;

import org.domain.Company;
import org.domain.TechnicalCheck;
import org.domain.TechnicalCheckImpl;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = TechnicalCheckMapper.class)
public abstract class TechnicalCheckMapper {

    public abstract TechnicalCheckRepresentation toRepresentation(TechnicalCheckImpl technicalCheck);
    public abstract List<TechnicalCheckRepresentation> toRepresentationList(List<TechnicalCheckImpl> technicalCheck);

    public abstract TechnicalCheckImpl toModel(TechnicalCheckRepresentation representation);
}
