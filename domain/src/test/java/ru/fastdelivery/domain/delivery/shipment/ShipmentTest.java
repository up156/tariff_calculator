package ru.fastdelivery.domain.delivery.shipment;

import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentTest {

    @Test
    void whenSummarizingWeightOfAllPackages_thenReturnSum() {
        var weight1 = new Weight(BigInteger.TEN);
        var weight2 = new Weight(BigInteger.ONE);
        var length = BigInteger.ONE;
        var width = BigInteger.ONE;
        var height = BigInteger.ONE;
        var departure = new Coordinates(73.555555, 55.222222);
        var destination = new Coordinates(53.555555, 35.222222);


        var packages = List.of(new Pack(weight1, length, width, height),
                new Pack(weight2, length, width, height));
        var shipment = new Shipment(packages, departure, destination, new CurrencyFactory(code -> true).create("RUB"));

        var massOfShipment = shipment.weightAllPackages();

        assertThat(massOfShipment.weightGrams()).isEqualByComparingTo(BigInteger.valueOf(11));
    }

    @Test
    void whenSummarizingVolumeOfAllPackages_thenReturnSum() {
        var weight1 = new Weight(BigInteger.TEN);
        var weight2 = new Weight(BigInteger.ONE);
        var length1 = BigInteger.valueOf(100);
        var width1 = BigInteger.valueOf(100);
        var height1 = BigInteger.valueOf(100);
        var length2 = BigInteger.valueOf(1000);
        var width2 = BigInteger.valueOf(1000);
        var height2 = BigInteger.valueOf(1000);
        var departure = new Coordinates(73.555555, 55.222222);
        var destination = new Coordinates(53.555555, 35.222222);


        var packages = List.of(new Pack(weight1, length1, width1, height1),
                new Pack(weight2, length2, width2, height2));
        var shipment = new Shipment(packages, departure, destination, new CurrencyFactory(code -> true).create("RUB"));

        var volumeOfShipment = shipment.getAllPackagesVolume();

        assertThat(volumeOfShipment).isEqualTo(BigDecimal.valueOf(1.001).setScale(4, RoundingMode.HALF_EVEN));
    }
}