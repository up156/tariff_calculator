package ru.fastdelivery.domain.delivery.pack;

import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.weight.Weight;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PackTest {

    @Test
    void whenWeightMoreThanMaxWeight_thenThrowException() {
        var weight = new Weight(BigInteger.valueOf(150_001));
        var length = BigInteger.ONE;
        var width = BigInteger.ONE;
        var height = BigInteger.ONE;

        assertThatThrownBy(() -> new Pack(weight, length, width, height))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void whenWeightLessThanMaxWeightAndCorrectDimensions_thenObjectCreated() {
        var length = BigInteger.ONE;
        var width = BigInteger.ONE;
        var height = BigInteger.ONE;
        var actual = new Pack(new Weight(BigInteger.valueOf(1_000)), length, width, height);
        assertThat(actual.weight()).isEqualTo(new Weight(BigInteger.valueOf(1_000)));
    }

    @Test
    void whenDimensionLessThanOne_thenThrowException() {
        var weight = new Weight(BigInteger.valueOf(100_000));
        var length = BigInteger.ONE;
        var width = BigInteger.ZERO;
        var height = BigInteger.ONE;
        assertThatThrownBy(() -> new Pack(weight, length, width, height))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void whenDimensionGreaterThan1500_thenThrowException() {
        var weight = new Weight(BigInteger.valueOf(100_000));
        var length = BigInteger.ONE;
        var width = BigInteger.ONE;
        var height = BigInteger.valueOf(1501);
        assertThatThrownBy(() -> new Pack(weight, length, width, height))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void whenGivenDimensionNotRound_thenDimensionsRounded() {
        var weight = new Weight(BigInteger.valueOf(100_000));
        var length = BigInteger.valueOf(500);
        var width = BigInteger.valueOf(520);
        var height = BigInteger.valueOf(340);
        var actual = new Pack(weight, length, width, height);
        assertThat(actual.length()).isEqualTo(BigInteger.valueOf(500));
        assertThat(actual.width()).isEqualTo(BigInteger.valueOf(550));
        assertThat(actual.height()).isEqualTo(BigInteger.valueOf(350));
    }


}