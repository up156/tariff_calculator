package ru.fastdelivery.properties_provider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.shipment.Coordinates;
import ru.fastdelivery.domain.delivery.shipment.Shipment;
import ru.fastdelivery.properties.provider.CoordinatesProperties;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CoordinatesPropertiesTest {
    CoordinatesProperties properties;
    @BeforeEach
    void init() {
        properties = new CoordinatesProperties();
        properties.setMinLatitude(45.0);
        properties.setMaxLatitude(65.0);
        properties.setMinLongitude(30.0);
        properties.setMaxLongitude(96.0);
        properties.setMinDestination(450);
    }

    @Test
    @DisplayName("Если корректные координаты -> true")
    void whenValidCoordinates_thanReturnTrue() {

        var weight1 = new Weight(BigInteger.TEN);
        var length = BigInteger.ONE;
        var width = BigInteger.ONE;
        var height = BigInteger.ONE;
        var departure = new Coordinates(53.555555, 55.222222);
        var destination = new Coordinates(53.555555, 35.222222);
        var packages = List.of(new Pack(weight1, length, width, height));
        var shipment = new Shipment(packages, departure, destination, new CurrencyFactory(code -> true).create("RUB"));

        assertTrue(properties.validCoordinates(shipment));
    }

    @Test
    @DisplayName("Если некорректные координаты -> Exception")
    void whenInvalidCoordinates_thanThrownException() {

        Exception thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {

            var weight1 = new Weight(BigInteger.TEN);
            var length = BigInteger.ONE;
            var width = BigInteger.ONE;
            var height = BigInteger.ONE;
            var departure = new Coordinates(173.555555, 55.222222);
            var destination = new Coordinates(53.555555, 35.222222);
            var packages = List.of(new Pack(weight1, length, width, height));
            var shipment = new Shipment(packages, departure, destination, new CurrencyFactory(code -> true).create("RUB"));
            properties.validCoordinates(shipment);
        });
        Assertions.assertEquals(thrown.getMessage(), "wrong coordinates!");
    }
}