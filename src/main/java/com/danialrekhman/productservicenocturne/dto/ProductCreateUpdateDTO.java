package com.danialrekhman.productservicenocturne.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateUpdateDTO {

    private String name;

    private String description;

    private BigDecimal price;

    private Long categoryId;
}