package com.epam.esm.utils.query;

import java.util.Locale;

public class SqlQuery {
    public static class GiftCertificate {

        public static final String GET_ALL_GIFT_CERTIFICATES_BY_TAG_NAME = "SELECT gc.id , gc.name , gc.description, gc.price, gc.duration ,gc.create_date ,gc.last_update_date FROM gift_certificate_tags gct  JOIN tag t JOIN gift_certificate gc  WHERE gc.id = gct.gift_certificate_id AND t.id = gct.tags_id AND t.name = ?1 LIMIT ?2,?3";
        public static final String GET_ALL_GIFT_CERTIFICATES_BY_NAME_OR_PART_OF_NAME = "SELECT * FROM  gift_certificate gc WHERE gc.name LIKE ?1 LIMIT ?2,?3";
        public static final String GET_ALL_GIFT_CERTIFICATES_BY_TAGS_AND_PRICE = """
                SELECT DISTINCT (gc.id) , gc.name , gc.description, gc.price, gc.duration ,gc.create_date ,gc.last_update_date
                FROM gift_certificate_tags gct  JOIN tag t JOIN gift_certificate gc
                WHERE gc.id = gct.gift_certificate_id AND t.id = gct.tags_id AND t.name IN (?1,?2) AND gc.price >= ?3
                group by gc.name, (gc.id), gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date
                having count(t.id) = 2
                LIMIT ?4,?5""";
    }

    public static class Tag {

        public static final String GET_ID_BY_TAG_NAME = "SELECT t.id FROM Tag t WHERE t.name = :name";
        public static final String GET_THE_MOST_WIDELY_USED_TAG_WITH_THE_HIGHEST_COST_OF_ALL_ORDERS = """
                select t.id, t.name
                                                                         from orders o
                                                                                   join users_orders uo on uo.orders_id = o.id and uo.user_id = (select u.id
                                                                                                                                                     from orders o,
                                                                                                                                                          users u,
                                                                                                                                                          users_orders uo
                                                                                                                                                     where (o.id = uo.orders_id
                                                                                                                                                         and u.id = uo.user_id)
                                                                                                                                                     group by u.id
                                                                                                                                                     order by sum(o.cost) desc
                                                                                                                                                     Limit 1)
                                                                                   join (gift_certificate_tags gct) on o.gift_certificate_id = gct.gift_certificate_id
                                                                                   join (tag t) on t.id = gct.tags_id
                                                                         group by t.name
                                                                         order by count(t.name) desc
                                                                         limit 1""";

    }
}
