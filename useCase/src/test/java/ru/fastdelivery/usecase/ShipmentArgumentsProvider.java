package ru.fastdelivery.usecase;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.shipment.Coordinates;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

public class ShipmentArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        var departure = new Coordinates(50.000005, 55.222222);
        var destination = new Coordinates(55.100005, 55.522222);

        return Stream.of(Arguments.of(new Shipment(List.of(new Pack(new Weight(BigInteger.valueOf(1200)), BigInteger.valueOf(100),
                        BigInteger.valueOf(100), BigInteger.valueOf(100))),
                        departure,
                        destination,
                        new CurrencyFactory(code -> true).create("RUB"))),
                Arguments.of(new Shipment(List.of(new Pack(new Weight(BigInteger.valueOf(120)), BigInteger.valueOf(100),
                        BigInteger.valueOf(100), BigInteger.valueOf(100))),
                        departure,
                        destination,
                        new CurrencyFactory(code -> true).create("RUB"))));
    }
}
