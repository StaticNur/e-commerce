package com.staticnur.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staticnur.model.Address;
import com.staticnur.services.AddressFormation;
import com.staticnur.services.EsService;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class EsServiceImpl implements EsService {

    private final static String INDEX_NAME = "addres";
    private final RestHighLevelClient esClient;
    private final ObjectMapper mapper;
    private final AddressFormation addressFormation;

    @Autowired
    public EsServiceImpl(RestHighLevelClient esClient, ObjectMapper mapper, AddressFormation addressFormation) {
        this.esClient = esClient;
        this.mapper = mapper;
        this.addressFormation = addressFormation;
    }

    /*SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
         SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

         String[] parts = query.split(" ");
         //String s1 = "Республика Мордовия, г.о. Саранск, г Саранск, ул Б.Хмельницкого, д. 14";
         // (?:Мордовия\s+)?(?:г\.\s+)?Саранск\s+ул\.\s+Ленина\s+14
         StringBuilder stringBuilder = new StringBuilder();
         stringBuilder.append("^(.*?)");
         for (String s: parts) {
             stringBuilder.append("(").append(s).append(")")
                     .append("(.*?)");
         }
         BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
         String address = stringBuilder.toString();

         if (!address.isEmpty()) {
             System.out.println("Address: " + address);
             RegexpQueryBuilder regexpQuery = QueryBuilders.regexpQuery("address", address);
             boolQuery.must(regexpQuery);
         }
         searchSourceBuilder.query(boolQuery);
         searchSourceBuilder.size(10000);
         searchRequest.source(searchSourceBuilder);

         SearchResponse searchResponse;
         try {
             searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
         return extractAddressesFromResponse(searchResponse);*/

    /*String s = "Респ Мордовия, г. Саранск, ул. Б.Хмельницкого, д. 14 кв. 14";
        String s1 = "Респ Мордовия, г. Саранск, ул. Б. Хмельницкого, д. 14";
        String s2 = "Респ Мордовия, г. Саранск, улица Б.Хмельницкого, д. 14";
        String s3 = "Республика Мордовия, г. Саранск, улица Б.Хмельницкого, д. 14";
        String s4 = "Республика Мордовия, г. Саранск, улица Б. Хмельницкого, дом 14, кв 14";
        String s5 = "Республика Мордовия, город Саранск, улица Б. Хмельницкого, дом 14, квартира 14";
        String s6 = "Мордовия, Саранск, Б.Хмельницкого, 14, 14";*/

    /*SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //Респ Мордовия, г Саранск, пр-кт Ленина
        String s = "Респ Мордовия, г Саранск, ул Б.Хмельницкого, д. 14, кв. 14";
        String pattern = "(.*?)" + s.replaceAll(", ", "(.*?)") + "(.*?)";
        QueryBuilder regexQuery = QueryBuilders.regexpQuery("address", "(.*?)(б\\.хмельницкого)(.*?)");


        query = query.toLowerCase();
        query = query.replaceFirst("республика", "респ");
        query = query.replaceAll("[,.;]", "");
        String[] addressParts = query.split(" ");

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        for (int i = 0; i < addressParts.length; i++) {
            System.out.println(addressParts[i]);
            if (addressParts[i].equals("д")) {
                i++;
                if (i < addressParts.length) {
                    boolQuery.must(QueryBuilders.matchPhraseQuery("address", "д. "+addressParts[i]));
                }
            }if (addressParts[i].equals("кв")) {
                i++;
                if (i < addressParts.length) {
                    boolQuery.must(QueryBuilders.matchPhraseQuery("address", "кв. "+addressParts[i]));
                }
            } else {
                boolQuery.must(QueryBuilders.regexpQuery("address", "(.*?)(" + addressParts[i] + ")(.*?)"));
            }
        }
        searchSourceBuilder.query(boolQuery);
        searchSourceBuilder.size(10000);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse;
        try {
            searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extractAddressesFromResponse(searchResponse);*/
    @Override
    public List<Address> searchAddress(String query) {
        /*SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

query = query.toLowerCase();
query = query.replaceFirst("республика", "респ");
query = query.replaceAll("[,.;]", "");
String[] addressParts = query.split(" ");

BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

//for (int i = 0; i < addressParts.length; i++) {
//System.out.println(addressParts[i]);
boolQuery.must(QueryBuilders.regexpQuery("address", "(.*?)" + "мордовия" + "(.*?)" + "саранск" + "(.*?)"));
//}

searchSourceBuilder.query(boolQuery);
searchSourceBuilder.size(10000);
searchRequest.source(searchSourceBuilder);

SearchResponse searchResponse;
try {
    searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
} catch (IOException e) {
    throw new RuntimeException(e);
}
return extractAddressesFromResponse(searchResponse);*/
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        query = query.toLowerCase();
        query = query.replaceFirst("республика", "респ");
        query = query.replaceAll("[,.;]", "");
        String[] addressParts = query.split(" ");

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        String houseNumber = null;
        String apartmentNumber = null;
        for (String part : addressParts) {
            System.out.println(part);
            if (part.matches("\\d+")) {
                if (houseNumber == null) {
                    houseNumber = part;
                } else {
                    apartmentNumber = part;
                }
            }
            boolQuery.must(QueryBuilders.regexpQuery("address", "(.*?)(" + part + ")(.*?)"));
        }
        if (houseNumber != null) {
            boolQuery.should(QueryBuilders.termQuery("address", houseNumber).boost(2));
        }
        if (apartmentNumber != null) {
            boolQuery.should(QueryBuilders.termQuery("address", apartmentNumber).boost(1));
        }

        searchSourceBuilder.query(boolQuery);
        searchSourceBuilder.size(10000);
        searchSourceBuilder.sort("_score", SortOrder.ASC);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse;
        try {
            searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extractAddressesFromResponse(searchResponse);
        /*SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        String[] parts = query.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(.*?)");
        for (String s: parts) {
            stringBuilder.append("(").append(s).append(")")
                    .append("(.*?)");
        }
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        String address = stringBuilder.toString();
        address = address.toLowerCase();
        if (!address.isEmpty()) {
            System.out.println("Address: " + address);
            RegexpQueryBuilder regexpQuery = QueryBuilders.regexpQuery("address", address);
            boolQuery.must(regexpQuery);
        }
        searchSourceBuilder.query(boolQuery);
        searchSourceBuilder.size(10000);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse;
        try {
            searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extractAddressesFromResponse(searchResponse);*/
        /*SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("address", query));
        searchSourceBuilder.size(10000);

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse;
        try {
            searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extractAddressesFromResponse(searchResponse);*/
    }

    @Override
    public void download() {
        long batchSize = 10_000;
        long fromIndex = 0;
        long toIndex = batchSize;

        //while (true) {
        List<Address> addressBatch = addressFormation.getAddress(fromIndex, toIndex);
        if (addressBatch.isEmpty()) {
            //break;
        }

        for (; fromIndex < addressBatch.size(); fromIndex += batchSize) {
            toIndex = Math.min(fromIndex + batchSize, addressBatch.size());

            List<Address> batch = addressBatch.subList((int) fromIndex, (int) toIndex);

            BulkRequest bulkRequest = prepareBulkRequest(batch);
            executeBulkRequest(bulkRequest);
        }


        fromIndex += batchSize;
        toIndex += batchSize;
        System.out.println("Size: " + toIndex);//Size: 552_257
        //}
    }

    private List<Address> extractAddressesFromResponse(SearchResponse searchResponse) {
        List<Address> addresses = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Address address = getAddress(hit);
            addresses.add(address);
        }
        return addresses;
    }

    private Address getAddress(SearchHit hit) {
        Map<String, Object> sourceAsMap = hit.getSourceAsMap();
        return Address.builder().address(sourceAsMap.getOrDefault("address", "").toString()).path(sourceAsMap.getOrDefault("path", "").toString()).build();
    }

    private BulkRequest prepareBulkRequest(List<Address> allAddress) {
        BulkRequest bulkRequest = new BulkRequest();
        for (Address address : allAddress) {
            IndexRequest indexRequest = new IndexRequest(INDEX_NAME);
            indexRequest.id(UUID.randomUUID().toString());
            try {
                indexRequest.source(mapper.writeValueAsString(address), XContentType.JSON);
                bulkRequest.add(indexRequest);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return bulkRequest;
    }

    private void executeBulkRequest(BulkRequest bulkRequest) {
        try {
            BulkResponse bulkResponse = esClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            handleBulkResponse(bulkResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleBulkResponse(BulkResponse bulkResponse) {
        if (bulkResponse.hasFailures()) {
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                if (bulkItemResponse.isFailed()) {
                    BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                    throw new RuntimeException("Failed to process request: " + failure.getMessage());
                }
            }
        }
    }

    public String normalizeAddress(String address) {
        String normalizedAddress = address.replaceAll("[,.;]", "");

        normalizedAddress = normalizedAddress.replaceAll("\\bг(.*?)\\b", "г. ");
        normalizedAddress = normalizedAddress.replaceAll("\\bд(.*?)\\b", "д. ");
        normalizedAddress = normalizedAddress.replaceAll("\\bкв(.*?)\\b", "кв. ");
        return normalizedAddress;
    }

}
