package com.example.springnews.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PageDTO {
    private int totalPage;
    private int startPage;
    private int endPage;
    private int currentPage;
    private int size;
    private boolean prev;
    private boolean next;
}
