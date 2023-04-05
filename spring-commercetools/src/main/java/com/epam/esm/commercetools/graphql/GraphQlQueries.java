package com.epam.esm.commercetools.graphql;

public class GraphQlQueries {

    public static final String getAllGiftCertificatesByTagName = """
            query($where:String!) {
              products (where: $where){
                	results{
                    id
                    createdAt
                    lastModifiedAt
                    masterData{
                        current{
                            nameAllLocales{
                                value
                            }
                            descriptionAllLocales{
                                value
                            }
                            masterVariant{
                                prices{
                                    value{
                                        currencyCode
                                        centAmount
                                    }
                                }
                                attributesRaw   {
                                    name
                                    value
                                }
                            }
                        }
                    }
                  }
                }
            }
            """;
}
