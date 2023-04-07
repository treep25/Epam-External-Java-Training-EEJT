package com.epam.esm.commercetools.graphql;

public class GraphQlQueries {

    public static final String getAllGiftCertificatesByTagName = """
            query($where:String! $limit:Int! $offset:Int!) {
              products (offset:$offset limit: $limit where: $where){
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

    public static String getVariableForTagName(String tagName) {
        return "masterData(current(masterVariant(attributes(value = \"" + tagName + "\"))))";
    }
}
