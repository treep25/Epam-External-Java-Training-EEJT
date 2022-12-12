package com.epam.esm;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.model.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {
    private final GiftCertificateDAO giftCertificateDAO;

    @Autowired
    public GiftCertificateController(GiftCertificateDAO giftCertificateDAO) {
        this.giftCertificateDAO = giftCertificateDAO;
    }

    @GetMapping(value = "/getAll")
    public List<Map<Long, GiftCertificate>> getAll() {
        return giftCertificateDAO.getAllGiftCertificates();
    }
}
