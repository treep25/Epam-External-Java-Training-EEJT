package com.epam.esm.utils.query;

import java.util.Locale;

public class SqlQuery {
    public static class GiftCertificate {

        public static final String IS_GIFT_CERTIFICATE_EXISTS_BY_NAME = "select case when count(gc)> 0 then true else false end from GiftCertificate gc where gc.name = :name";
        public static final String GET_ALL_GIFT_CERTIFICATES_BY_TAG_NAME = "SELECT gc.id , gc.name , gc.description, gc.price, gc.duration ,gc.create_date ,gc.last_update_date FROM gift_certificate_tags gct  JOIN tag t JOIN gift_certificate gc  WHERE gc.id = gct.gift_certificate_id AND t.id = gct.tags_id AND t.name = ?1 LIMIT ?2,?3";
        public static final String GET_ALL_GIFT_CERTIFICATES_BY_NAME_OR_PART_OF_NAME = "SELECT * FROM  gift_certificate gc WHERE gc.name LIKE ?1 LIMIT ?2,?3";
        public static final String GET_ALL_GIFT_CERTIFICATES_BY_TAGS_AND_PRICE = "SELECT DISTINCT (gc.id) , gc.name , gc.description, gc.price, gc.duration ,gc.create_date ,gc.last_update_date FROM gift_certificate_tags gct  JOIN tag t JOIN gift_certificate gc  WHERE gc.id = gct.gift_certificate_id AND t.id = gct.tags_id AND (t.name = ?1 OR t.name =?2) AND gc.price >= ?3 LIMIT ?4,?5";
    }

    public static class Tag {

        public static final String GET_ID_BY_TAG_NAME = "SELECT t.id FROM Tag t WHERE t.name = :name";
        public static final String IS_TAG_EXISTS_BY_NAME = "select case when count(t)> 0 then true else false end from Tag t where t.name = :name";
        public static final String GET_THE_MOST_WIDELY_USED_TAG_WITH_THE_HIGHEST_COST_OF_ALL_ORDERS = "SELECT *\n" +
                "FROM (SELECT t.id, t.name\n" +
                "      FROM orders o\n" +
                "               INNER JOIN user_orders uo on uo.orders_id = o.id AND uo.user_id = (SELECT user.id\n" +
                "                                                                                  FROM (SELECT res.id, res.sumOfAllUsersOrders\n" +
                "                                                                                        FROM (SELECT u.id, SUM(o.cost) as sumOfAllUsersOrders\n" +
                "                                                                                              FROM orders o,\n" +
                "                                                                                                   user u,\n" +
                "                                                                                                   user_orders uo\n" +
                "                                                                                              WHERE o.id = uo.orders_id\n" +
                "                                                                                                AND u.id = uo.user_id\n" +
                "                                                                                              GROUP BY u.id\n" +
                "                                                                                              ORDER BY sumOfAllUsersOrders DESC) res\n" +
                "                                                                                        having sumOfAllUsersOrders = MAX(sumOfAllUsersOrders)) user)\n" +
                "               INNER JOIN gift_certificate_tags gct on o.gift_certificate_id = gct.gift_certificate_id\n" +
                "               INNER JOIN tag t on t.id = gct.tags_id) t\n" +
                "GROUP BY t.name\n" +
                "ORDER BY COUNT(t.name) DESC\n" +
                "LIMIT 1;";

    }
}
