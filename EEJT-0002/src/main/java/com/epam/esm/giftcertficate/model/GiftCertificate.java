package com.epam.esm.giftcertficate.model;

import com.epam.esm.tag.model.Tag;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class GiftCertificate {
    private Long id;
    private String name;
    private List<Tag> tags;
    private String description;
    private Integer price;
    private Integer duration;
    private String createDate;

    private String lastUpdateDate;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Tag> getTags() {
        return tags;
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

    public String getCreateDate() {
        return createDate;
    }

    public String getLastUpdateDate() {
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

    public GiftCertificate setTags(List<Tag> tags) {
        this.tags = tags;
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
        this.createDate = getDateIso(createDate);
        return this;
    }

    public GiftCertificate setLastUpdateDate(Date lastUpdateDate) {

        this.lastUpdateDate = getDateIso(lastUpdateDate);
        return this;
    }

    public GiftCertificate setCreateDate(String createDate) {
        this.createDate = createDate;
        return this;
    }

    public GiftCertificate setLastUpdateDate(String lastUpdateDate) {

        this.lastUpdateDate = lastUpdateDate;
        return this;
    }

    private String getDateIso(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        return df.format(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof GiftCertificate)) return false;

        GiftCertificate that = (GiftCertificate) o;

        return new EqualsBuilder().append(getId(),
                that.getId()).append(getName(),
                that.getName()).append(getTags(),
                that.getTags()).append(getDescription(),
                that.getDescription()).append(getPrice(),
                that.getPrice()).append(getDuration(),
                that.getDuration()).append(getCreateDate(),
                that.getCreateDate()).append(getLastUpdateDate(),
                that.getLastUpdateDate()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getId()).append(getName())
                .append(getTags()).append(getDescription())
                .append(getPrice()).append(getDuration())
                .append(getCreateDate())
                .append(getLastUpdateDate()).toHashCode();
    }

    @Override
    public String toString() {
        return "GiftCertificate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate='" + createDate + '\'' +
                ", lastUpdateDate='" + lastUpdateDate + '\'' +
                '}';
    }
}
