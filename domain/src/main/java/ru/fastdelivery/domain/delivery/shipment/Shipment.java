package ru.fastdelivery.domain.delivery.shipment;

import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;

import java.math.BigDecimal;
import java.util.List;

/**
 * @param packages упаковки в грузе
 * @param currency валюта объявленная для груза
 * @param departure координаты пункта отправки
 * @param destination координаты пункта назначения
 */
public record Shipment(
        List<Pack> packages,
        Coordinates departure,
        Coordinates destination,
        Currency currency

) {
    public Weight weightAllPackages() {
        return packages.stream()
                .map(Pack::weight)
                .reduce(Weight.zero(), Weight::add);
    }

    public BigDecimal getAllPackagesVolume() {
        return packages.stream()
                .map(Pack::getVolume)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
