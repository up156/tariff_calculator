package ru.fastdelivery.presentation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigInteger;

public record CargoPackage(
        @Schema(description = "Вес упаковки, граммы", example = "5667")
        BigInteger weight,
        @Schema(description = "Длина упаковки, мм", example = "345")
        BigInteger length,
        @Schema(description = "Ширина упаковки, мм", example = "589")
        BigInteger width,
        @Schema(description = "Вес упаковки, граммы", example = "234")
        BigInteger height
) {
}
