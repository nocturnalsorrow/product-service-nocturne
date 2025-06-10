package com.danialrekhman.productservicenocturne.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long id;

    private String name;

    private Long parentId; // чтобы не возвращать весь объект parent, а только id

    private List<CategoryDTO> subcategories; // вложенные подкатегории (можно пустой список)
}