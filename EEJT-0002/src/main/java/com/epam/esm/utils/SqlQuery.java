package com.epam.esm.utils;

import java.util.List;
import java.util.Locale;

public class SqlQuery {
    public static class GiftCertificate {
        public static final String GET_ALL_CERTIFICATES = "SELECT * FROM gift_certificate";
        public static final String GET_CERTIFICATE_BY_ID = "SELECT * FROM gift_certificate WHERE id=?";
        public static final String CREATE_CERTIFICATE = "INSERT INTO gift_certificate(name,description,price,duration,create_date,last_update_date) VALUES(?,?,?,?,?,?)";
        public static final String DELETE_CERTIFICATE = "DELETE  FROM gift_certificate WHERE id=?";
    }

    public static class TagGiftCertificate {
        public static String getTypeOfSortingForDate(String method) {
            if ("DESC".equals(method.toUpperCase(Locale.ROOT))) {
                return "SELECT t.id, t.name, gc.id, gc.name, description, price, duration, create_date, last_update_date FROM  gift_certificate_tag gct INNER JOIN tag t ON gct.tag_id = t.id INNER JOIN gift_certificate gc ON gct.gift_certificate_id = gc.id ORDER BY gc.create_date DESC";
            }
            return "SELECT t.id, t.name, gc.id, gc.name, description, price,duration, create_date, last_update_date FROM  gift_certificate_tag gct INNER JOIN tag t ON gct.tag_id = t.id INNER JOIN gift_certificate gc ON gct.gift_certificate_id = gc.id ORDER BY gc.create_date";
        }

        public static String getTypeOfSortingForDateAndName(String method) {
            if ("DESC".equals(method.toUpperCase(Locale.ROOT))) {
                return "SELECT t.id, t.name, gc.id, gc.name, description, price, duration, create_date, last_update_date FROM  gift_certificate_tag gct INNER JOIN tag t ON gct.tag_id = t.id INNER JOIN gift_certificate gc ON gct.gift_certificate_id = gc.id ORDER BY gc.create_date AND gc.name DESC";
            }
            return "SELECT t.id, t.name, gc.id, gc.name, description, price, duration, create_date, last_update_date FROM  gift_certificate_tag gct INNER JOIN tag t ON gct.tag_id = t.id INNER JOIN gift_certificate gc ON gct.gift_certificate_id = gc.id ORDER BY gc.create_date AND gc.name";
        }
    }
}
