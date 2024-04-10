package org.application;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.domain.TechnicalCheck.TechnicalCheckImpl;
import org.domain.TechnicalCheck.TechnicalCheckRepository;
import org.infastructure.service.fleet.representation.VehicleRepresentation;
import org.util.DamageType;

@RequestScoped
public class TechnicalCheckService {

    @Inject
    FleetService fleetService;

    @Inject
    TechnicalCheckRepository technicalCheckRepository;

    @Transactional
    public DamageType doTechnicalCheck(Integer vehicleId, Integer technicalCheckId) {
        VehicleRepresentation vehicle = fleetService.vehicleById(vehicleId);
        if (vehicle.id == null) {
            return null;
        }

        TechnicalCheckImpl technicalCheck = (TechnicalCheckImpl) technicalCheckRepository.findTechnicalCheckByIdOptional(technicalCheckId)
                .orElseThrow(() -> new NotFoundException("[!]TechnicalCheckService.doTechnicalCheck: Could not find technical check"));

        DamageType damageType = technicalCheck.checkForDamage(vehicle);
        return damageType;
    }
}
