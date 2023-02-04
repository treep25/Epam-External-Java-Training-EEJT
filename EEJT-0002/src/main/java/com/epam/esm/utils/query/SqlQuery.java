package com.epam.esm.utils.query;

import java.util.List;
import java.util.Locale;

public class SqlQuery {
    public static class GiftCertificate {
        public static final String GET_ALL_CERTIFICATES = "SELECT * FROM gift_certificate";
        public static final String GET_CERTIFICATE_BY_ID = "SELECT * FROM gift_certificate WHERE id=?";
        public static final String CREATE_CERTIFICATE = "INSERT INTO gift_certificate(name,description,price,duration,create_date,last_update_date) VALUES(?,?,?,?,?,?)";
        public static final String DELETE_CERTIFICATE = "DELETE  FROM gift_certificate WHERE id=?";
        public static final String GET_ALL_TAGS_BY_CERTIFICATE_ID = "SELECT  t.id,t.name FROM gift_certificate_tag gct  JOIN tag t  WHERE gct.gift_certificate_id = ? AND t.id = gct.tag_id";
        public static final String GET_ID_BY_GIFT_CERTIFICATE = "SELECT id FROM gift_certificate WHERE name = ? AND description = ? AND price = ? AND duration = ?";
        public static final String IS_GIFT_CERTIFICATE_EXIST = "SELECT EXISTS(SELECT * FROM gift_certificate WHERE name=? AND description = ? AND price = ? AND duration = ?)";
        public static final String CREATE_GIFT_CERTIFICATE_TAG = "INSERT INTO gift_certificate_tag (gift_certificate_id,tag_id) VALUES (?,?)";
        public static final String DELETE_GIFT_CERTIFICATE_TAG = "DELETE  FROM gift_certificate_tag WHERE gift_certificate_id = ? AND tag_id = ?";
    }

    public static class TagGiftCertificate {
        public static String getTypeOfSortingForDate(String method) {
            if ("DESC".equals(method.toUpperCase(Locale.ROOT))) {
                return "SELECT  gc.id, gc.name, description, price, duration, create_date, last_update_date FROM  gift_certificate gc  ORDER BY gc.create_date DESC";
            }
            return "SELECT  gc.id, gc.name, description, price, duration, create_date, last_update_date FROM  gift_certificate gc  ORDER BY gc.create_date";
        }

        public static String getTypeOfSortingForDateAndName(String method1, String method2) {
            if ("DESC".equals(method1.toUpperCase(Locale.ROOT)) && "ASC".equals(method2.toUpperCase(Locale.ROOT))) {

                return "SELECT  gc.id, gc.name, description, price, duration, create_date, last_update_date FROM  gift_certificate gc  ORDER BY gc.name DESC , gc.create_date";
            } else if ("ASC".equals(method1.toUpperCase(Locale.ROOT)) && "DESC".equals(method2.toUpperCase(Locale.ROOT))) {

                return "SELECT  gc.id, gc.name, description, price, duration, create_date, last_update_date FROM  gift_certificate gc  ORDER BY gc.name , gc.create_date DESC";
            } else if ("DESC".equals(method1.toUpperCase(Locale.ROOT)) && "DESC".equals(method2.toUpperCase(Locale.ROOT))) {

                return "SELECT  gc.id, gc.name, description, price, duration, create_date, last_update_date FROM  gift_certificate gc  ORDER BY gc.name DESC, gc.create_date DESC";
            }

            return "SELECT  gc.id, gc.name, description, price, duration, create_date, last_update_date FROM  gift_certificate gc  ORDER BY gc.name, gc.create_date";
        }

        public static final String GET_GIFT_CERTIFICATE_TAGS_BY_TAG_NAME = "SELECT   gc.id , gc.name , gc.description, gc.price, gc.duration ,gc.create_date ,gc.last_update_date FROM gift_certificate_tag gct  JOIN tag t JOIN gift_certificate gc  WHERE gc.id = gct.gift_certificate_id AND t.id = gct.tag_id AND t.name = ?";
        public static final String GET_GIFT_CERTIFICATE_AND_TAGS_BY_NAME_OR_BY_PART_OF_NAME = "SELECT  gc.id, gc.name, description, price,duration, create_date, last_update_date FROM  gift_certificate gc WHERE gc.name LIKE ?";
        public static final String GET_ALL_TAGS_BY_GIFT_CERTIFICATE_ID_ORDER_BY_TAG_NAME = "SELECT  t.id,t.name FROM gift_certificate_tag gct  JOIN tag t  WHERE gct.gift_certificate_id = ? AND t.id = gct.tag_id ORDER BY  t.name = ? DESC";
        public static final String GET_ALL_TAGS_BY_GIFT_CERTIFICATE_ID = "SELECT  t.id,t.name FROM gift_certificate_tag gct  JOIN tag t  WHERE gct.gift_certificate_id = ? AND t.id = gct.tag_id";


    }

    public static class Tag {
        public static final String CREATE_TAG = "INSERT INTO tag (name) VALUES(?)";
        public static final String DELETE_TAG = "DELETE  FROM tag WHERE id=?";
        public static final String GET_ALL_TAGS = "SELECT * FROM tag";
        public static final String GET_TAG_BY_ID = "SELECT * FROM tag WHERE id = ?";
        public static final String IS_TAG_WITH_NAME_EXISTS = "SELECT EXISTS(SELECT * FROM tag WHERE name=?)";
        public static final String GET_ID_BY_TAG = "SELECT id FROM tag WHERE name = ?";
        public static final String GET_ALL_TAGS_BY_CERTIFICATE_ID = "SELECT  t.id,t.name FROM gift_certificate_tag gct  JOIN tag t  WHERE gct.gift_certificate_id = ?  AND t.id = gct.tag_id";

    }
}
