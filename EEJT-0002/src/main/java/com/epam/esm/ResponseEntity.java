package com.epam.esm;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.repository.SqlQuery;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ResponseEntity {

    private List<GiftCertificate> giftCertificateList;
    private final HttpStatus status;
    private GiftCertificate giftCertificate;

    public ResponseEntity(List<GiftCertificate> giftCertificateList, HttpStatus status) {
        this.giftCertificateList = giftCertificateList;
        this.status = status;
    }

    public ResponseEntity(GiftCertificate giftCertificate, HttpStatus status) {
        this.status = status;
        this.giftCertificate = giftCertificate;
    }

    public List<GiftCertificate> getCertificates() {
        return giftCertificateList;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
