package com.epam.esm.model;

import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class GiftCertificate {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private Integer duration;
    private Date createDate;

    private Date lastUpdateDate;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getDuration() {
        return duration;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public GiftCertificate setId(Long id) {
        this.id = id;
        return this;
    }

    public GiftCertificate setName(String name) {
        this.name = name;
        return this;
    }

    public GiftCertificate setDescription(String description) {
        this.description = description;
        return this;
    }

    public GiftCertificate setPrice(Integer price) {
        this.price = price;
        return this;
    }

    public GiftCertificate setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public GiftCertificate setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    public GiftCertificate setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
        return this;
    }

    @Override
    public String toString() {
        return "GiftCertificate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
