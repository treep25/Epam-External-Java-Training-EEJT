package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;

import java.util.List;
import java.util.Map;

public interface GiftCertificateDAO {
    List<Map<Long, GiftCertificate>> getAllGiftCertificates();
}
