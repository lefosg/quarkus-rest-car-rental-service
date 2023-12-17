package org.util;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.domain.Vehicle;
import org.junit.jupiter.api.Test;
import org.representation.VehicleMapper;
import org.representation.VehicleRepresentation;

/**
 * Just for random testing. Ignore
 */
@QuarkusTest
public class PlaygroundTest {

    @Inject
    VehicleMapper vehicleMapper;

    @Test
    public void test() {
        Vehicle vehicle1 = new Vehicle("TOYOTA", "YARIS", 2015, 100000, "YMB-6325", VehicleType.Hatchback, new Money(30));
        VehicleRepresentation representation = vehicleMapper.toRepresentation(vehicle1);
        System.out.println(representation);

        Vehicle v = vehicleMapper.toModel(representation);
        System.out.println(v);


    }


}
