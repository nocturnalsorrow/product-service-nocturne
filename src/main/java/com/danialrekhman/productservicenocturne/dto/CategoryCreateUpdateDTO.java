package com.danialrekhman.productservicenocturne.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateUpdateDTO {

    private String name;

    private Long parentId;  // при создании/обновлении можно указать родителя по id
}