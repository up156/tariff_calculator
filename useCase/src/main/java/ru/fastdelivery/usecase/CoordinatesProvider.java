package ru.fastdelivery.usecase;

import ru.fastdelivery.domain.delivery.shipment.Shipment;

public interface CoordinatesProvider {

    Boolean validCoordinates(Shipment shipment);

    Integer minDestination();
}
