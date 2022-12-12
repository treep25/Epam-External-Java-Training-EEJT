package com.epam.esm.model;

import org.springframework.stereotype.Component;

@Component
public class Tag {
    private Long id;
    private String name;

    public String getName() {
        return name;
    }
}
