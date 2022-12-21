package com.epam.esm.tag;

import com.epam.esm.giftcertficate.GiftCertificate;

public class Tag {
    private Long id;
    private String name;

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public Tag setId(Long id) {
        this.id = id;
        return this;
    }

    public Tag setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
