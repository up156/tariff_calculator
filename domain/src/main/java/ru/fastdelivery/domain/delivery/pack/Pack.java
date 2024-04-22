package ru.fastdelivery.domain.delivery.pack;

import ru.fastdelivery.domain.common.weight.Weight;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.stream.Stream;

/**
 * Упаковка груза
 *
 * @param weight вес товаров в упаковке
 */
public record Pack(Weight weight, BigInteger length, BigInteger width, BigInteger height) {

    private static final Weight maxWeight = new Weight(BigInteger.valueOf(150_000));


    public Pack {
        if (weight.greaterThan(maxWeight)) {
            throw new IllegalArgumentException("Package can't be more than " + maxWeight);
        }
        if (Stream.of(length, width, height).anyMatch(this::checkDimension)) {
            throw new IllegalArgumentException("incorrect dimension. Any side should be from 1mm to 1500mm!");
        }
        length = roundDimension(length);
        width = roundDimension(width);
        height = roundDimension(height);
    }

    public BigDecimal getVolume() {
        BigDecimal result = BigDecimal.valueOf(length.intValue() * width.intValue() * height.intValue() / 1_000_000_000.0);
        return result.setScale(4, RoundingMode.HALF_EVEN);
    }

    public BigInteger roundDimension(BigInteger side) {
        return side.remainder(BigInteger.valueOf(50)).equals(BigInteger.ZERO) ? side :
                side.divide(BigInteger.valueOf(50)).add(BigInteger.ONE).multiply(BigInteger.valueOf(50));
    }

    private Boolean checkDimension(BigInteger dimension) {
        return dimension.compareTo(BigInteger.ZERO) <= 0 || dimension.compareTo(BigInteger.valueOf(1500)) >= 0;

    }
}
