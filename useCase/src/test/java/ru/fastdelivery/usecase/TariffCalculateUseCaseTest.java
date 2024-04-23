package ru.fastdelivery.usecase;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@Slf4j
class TariffCalculateUseCaseTest {

    final PriceProvider priceProvider = mock(PriceProvider.class);

    final CoordinatesProvider coordinatesProvider = mock(CoordinatesProvider.class);
    final Currency currency = new CurrencyFactory(code -> true).create("RUB");

    final TariffCalculateUseCase tariffCalculateUseCase = new TariffCalculateUseCase(priceProvider, coordinatesProvider);

    @ParameterizedTest(name = "{index} - {0} расчет доставки: успешно")
    @ArgumentsSource(ShipmentArgumentsProvider.class)
    @DisplayName("Расчет стоимости доставки -> успешно")
    void whenCalculatePrice_thenSuccess(Shipment shipment) {
        var minimalPrice = new Price(BigDecimal.TEN, currency);
        var pricePerKg = new Price(BigDecimal.valueOf(100), currency);
        var pricePerCubicMetre = new Price(BigDecimal.valueOf(120000), currency);
        var minDestination = Integer.valueOf(10000);

        when(priceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(priceProvider.costPerKg()).thenReturn(pricePerKg);
        when(priceProvider.costPerCubicMetre()).thenReturn(pricePerCubicMetre);
        when(coordinatesProvider.validCoordinates(shipment)).thenReturn(true);
        when(coordinatesProvider.minDestination()).thenReturn(minDestination);

        var expectedPrice = new Price(BigDecimal.valueOf(120), currency);

        var actualPrice = tariffCalculateUseCase.calc(shipment);

        assertThat(actualPrice).usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(expectedPrice);
    }
    @ParameterizedTest(name = "{index} - {0} расчет доставки: успешно")
    @ArgumentsSource(ShipmentArgumentsProvider.class)
    @DisplayName("Расчет стоимости доставки с учетом координат -> успешно")
    void whenCalculatePriceWithCoordinates_thenSuccess(Shipment shipment) {
        var minimalPrice = new Price(BigDecimal.TEN, currency);
        var pricePerKg = new Price(BigDecimal.valueOf(100), currency);
        var pricePerCubicMetre = new Price(BigDecimal.valueOf(120000), currency);
        var minDestination = Integer.valueOf(450);

        when(priceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(priceProvider.costPerKg()).thenReturn(pricePerKg);
        when(priceProvider.costPerCubicMetre()).thenReturn(pricePerCubicMetre);
        when(coordinatesProvider.validCoordinates(shipment)).thenReturn(true);
        when(coordinatesProvider.minDestination()).thenReturn(minDestination);

        var expectedPrice = new Price(BigDecimal.valueOf(151.47), currency);

        var actualPrice = tariffCalculateUseCase.calc(shipment);

        assertThat(actualPrice).usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("Получение минимальной стоимости -> успешно")
    void whenMinimalPrice_thenSuccess() {
        BigDecimal minimalValue = BigDecimal.TEN;
        var minimalPrice = new Price(minimalValue, currency);
        when(priceProvider.minimalPrice()).thenReturn(minimalPrice);

        var actual = tariffCalculateUseCase.minimalPrice();

        assertThat(actual).isEqualTo(minimalPrice);
    }
}