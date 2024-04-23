package ru.fastdelivery.properties.provider;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.fastdelivery.domain.delivery.shipment.Shipment;
import ru.fastdelivery.usecase.CoordinatesProvider;

import java.util.stream.Stream;

/**
 * Настройки координат широты и долготы
 */

@ConfigurationProperties("coordinates")
@Setter
public class CoordinatesProperties implements CoordinatesProvider {

    private Double minLatitude;
    private Double maxLatitude;
    private Double minLongitude;
    private Double maxLongitude;
    private Integer minDestination;

    @Override
    public Boolean validCoordinates(Shipment shipment) {
        return Stream.of(shipment.departure(), shipment.destination())
                .map(e -> {
                    if (!(e.latitude() >= minLatitude && e.latitude() <= maxLatitude
                            && e.longitude() >= minLongitude && e.longitude() <= maxLongitude)) {
                        throw new IllegalArgumentException("wrong coordinates!");
                    }
                    return true;
                })
                .distinct().count() == 1;
    }

    @Override
    public Integer minDestination() {

        return minDestination;
    }
}
