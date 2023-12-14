package org.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.util.DamageType;

import java.util.Map;

@RegisterForReflection
public class ChargingPolicyRepresentation {
    public Long id;
    public Map<Integer, Float> mileageScale;
    public Map<DamageType, Float> damageType;
}
