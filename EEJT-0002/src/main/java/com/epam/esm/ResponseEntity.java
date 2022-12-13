package com.epam.esm;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.repository.SqlQuery;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ResponseEntity implements Response<List<GiftCertificate>> {

    private List<GiftCertificate> giftCertificateList;
    private HttpStatus status;

    public ResponseEntity(List<GiftCertificate> giftCertificateList, HttpStatus status) {
        this.giftCertificateList = giftCertificateList;
        this.status = status;
    }

    @Override
    public List<GiftCertificate> getEntity() {
        return null;
    }
}
