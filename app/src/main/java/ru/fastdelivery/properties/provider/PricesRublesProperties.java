package ru.fastdelivery.properties.provider;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.usecase.PriceProvider;

import java.math.BigDecimal;

/**
 * Настройки базовых цен стоимости перевозки из конфига
 */
@ConfigurationProperties("cost.rub")
@Setter
public class PricesRublesProperties implements PriceProvider {

    private BigDecimal perKg;

    private BigDecimal perCubicMetre;
    private BigDecimal minimal;

    @Autowired
    private CurrencyFactory currencyFactory;

    @Override
    public Price costPerKg() {
        return new Price(perKg, currencyFactory.create("RUB"));
    }

    @Override
    public Price costPerCubicMetre() {
        return new Price(perCubicMetre, currencyFactory.create("RUB"));
    }

    @Override
    public Price minimalPrice() {
        return new Price(minimal, currencyFactory.create("RUB"));
    }
}
