package org.infastructure.rest.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.util.DamageType;

import java.util.Map;

@RegisterForReflection
public class ChargingPolicyRepresentation {
    public Integer id;
    public Map<Integer, Float> mileageScale;
    public Map<DamageType, Float> damageType;
}
