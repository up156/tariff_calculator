package ru.fastdelivery.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import javax.inject.Named;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.stream.Stream;

@Named
@Slf4j
@RequiredArgsConstructor
public class TariffCalculateUseCase {
    private final PriceProvider priceProvider;

    private final CoordinatesProvider coordinatesProvider;

    public Price calc(Shipment shipment) {

        log.info("Calc invoked in TariffCalculateUseCase with shipment:  {}", shipment);
        if (!coordinatesProvider.validCoordinates(shipment)) {
            throw new IllegalArgumentException("некорректные координаты!");
        }

        var minimalPrice = priceProvider.minimalPrice();
        var volumePrice = getVolumePrice(shipment.getAllPackagesVolume());
        var weightPrice = getWeightPrice(shipment.weightAllPackages().kilograms());
        var destination = getDestinationBetweenCoordinates(shipment);
        var minDestination = coordinatesProvider.minDestination();

        log.info("Calc in TariffCalculateUseCase calculated volumePrice:  {}, weightPrice: {}", volumePrice, weightPrice);

        return Stream.of(minimalPrice, volumePrice, weightPrice)
                .max(Comparator.comparing(Price::amount))
                .orElse(minimalPrice)
                .multiply(BigDecimal.valueOf(Math.max(destination / Double.valueOf(minDestination), 1)))
                .roundUp();
    }

    public Price minimalPrice() {
        return priceProvider.minimalPrice();
    }

    public Price getWeightPrice(BigDecimal weightAllPackages) {
        return priceProvider
                .costPerKg()
                .multiply(weightAllPackages);
    }

    public Price getVolumePrice(BigDecimal volumeAllPackages) {
        return priceProvider
                .costPerCubicMetre()
                .multiply(volumeAllPackages);
    }

    private Integer getDestinationBetweenCoordinates(Shipment shipment) {
        // радиус Земли
        double rad = 6372795;

        // получение координат точек в радианах
        double lat1 = Math.toRadians(shipment.departure().latitude());
        double lat2 = Math.toRadians(shipment.destination().latitude());
        double long1 = Math.toRadians(shipment.departure().longitude());
        double long2 = Math.toRadians(shipment.destination().longitude());

        // косинусы и синусы широт и разницы долгот
        double cl1 = Math.cos(lat1);
        double cl2 = Math.cos(lat2);
        double sl1 = Math.sin(lat1);
        double sl2 = Math.sin(lat2);
        double delta = long2 - long1;
        double cDelta = Math.cos(delta);
        double sDelta = Math.sin(delta);

        // вычисления длины большого круга
        double y = Math.sqrt(Math.pow(cl2 * sDelta, 2) + Math.pow(cl1 * sl2 -sl1 * cl2 * cDelta,2));
        double x = sl1 * sl2 + cl1 * cl2 * cDelta;
        double ad = Math.atan2(y,x);
        double dist = ad * rad / 1000;
        log.info("getDestinationBetweenCoordinates invoked in TariffCalculateUseCase with result destination:  {}", dist);

        return BigDecimal.valueOf(dist).setScale(0, RoundingMode.HALF_EVEN).toBigInteger().intValue();
    }
}
