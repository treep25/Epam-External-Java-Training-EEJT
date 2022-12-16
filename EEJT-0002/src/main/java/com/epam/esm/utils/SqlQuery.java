package com.epam.esm.utils;

public class SqlQuery {
    public static class GiftCertificate {
        public static final String GET_ALL_CERTIFICATES = "SELECT * FROM gift_certificate";
        public static final String GET_CERTIFICATE_BY_ID = "SELECT * FROM gift_certificate WHERE id=?";
        public static final String CREATE_CERTIFICATE = "INSERT INTO gift_certificate(name,description,price,duration,create_date,last_update_date) VALUES(?,?,?,?,?,?)";
        public static final String DELETE_CERTIFICATE = "DELETE  FROM gift_certificate WHERE id=?";
        public static final String UPDATE_CERTIFICATE_BY_ID = "UPDATE gift_certificate SET name = ?, column2 = value2 WHERE condition";
    }
}
