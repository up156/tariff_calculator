package ru.fastdelivery.usecase;

import lombok.RequiredArgsConstructor;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import javax.inject.Named;

@Named
@RequiredArgsConstructor
public class TariffCalculateUseCase {
    private final PriceProvider priceProvider;

    public Price calc(Shipment shipment) {

        var weightAllPackagesKg = shipment.weightAllPackages().kilograms();
        var volumeAllPackages = shipment.getAllPackagesVolume();
        var minimalPrice = priceProvider.minimalPrice();
        var weightPrice = priceProvider
                .costPerCubicMetre()
                .multiply(volumeAllPackages)
                .max(minimalPrice);

        return priceProvider
                .costPerKg()
                .multiply(weightAllPackagesKg)
                .max(weightPrice);
    }

    public Price minimalPrice() {
        return priceProvider.minimalPrice();
    }
}
