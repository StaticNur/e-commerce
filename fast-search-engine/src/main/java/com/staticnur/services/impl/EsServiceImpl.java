package com.staticnur.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staticnur.dao.AddressDao;
import com.staticnur.model.Address;
import com.staticnur.services.EsService;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class EsServiceImpl implements EsService {

    private final static String INDEX_NAME = "address";
    private final RestHighLevelClient esClient;
    private final ObjectMapper mapper;
    private final AddressDao addressDao;

    @Autowired
    public EsServiceImpl(RestHighLevelClient esClient, ObjectMapper mapper, AddressDao addressDao) {
        this.esClient = esClient;
        this.mapper = mapper;
        this.addressDao = addressDao;
    }

    @Override
    public List<Address> searchAddress(String query) {
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryStringQueryBuilder queryStringQuery = QueryBuilders.queryStringQuery(query)
                .field("city")
                .field("street")
                .field("house")
                .field("room")
                .analyzer("edge_ngram_analyzer");

        searchSourceBuilder.query(queryStringQuery);

        /*HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field(new HighlightBuilder.Field("city"))
                .field(new HighlightBuilder.Field("street"))
                .field(new HighlightBuilder.Field("house"))
                .field(new HighlightBuilder.Field("room"));

        searchSourceBuilder.highlighter(highlightBuilder);*/
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse;
        try {
            searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Address> addresses = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Address address = getAddress(hit);
            addresses.add(address);
        }

        return addresses;
    }

    private static Address getAddress(SearchHit hit) {
        Map<String, Object> sourceAsMap = hit.getSourceAsMap();
        String city = (String) sourceAsMap.get("city");
        String street = (String) sourceAsMap.get("street");
        String house = (String) sourceAsMap.get("house");
        String room = (String) sourceAsMap.get("room");

            /*HighlightField highlightFieldCity = hit.getHighlightFields().get("city");
            HighlightField highlightFieldStreet = hit.getHighlightFields().get("street");
            HighlightField highlightFieldHouse = hit.getHighlightFields().get("house");
            HighlightField highlightFieldRoom = hit.getHighlightFields().get("room");

            if (highlightFieldCity != null && highlightFieldCity.fragments().length > 0) {
                city = highlightFieldCity.fragments()[0].toString();
            }
            if (highlightFieldStreet != null && highlightFieldStreet.fragments().length > 0) {
                street = highlightFieldStreet.fragments()[0].toString();
            }
            if (highlightFieldHouse != null && highlightFieldHouse.fragments().length > 0) {
                house = highlightFieldHouse.fragments()[0].toString();
            }
            if (highlightFieldRoom != null && highlightFieldRoom.fragments().length > 0) {
                room = highlightFieldRoom.fragments()[0].toString();
            }*/

        Address address = new Address();
        address.setCity(city);
        address.setStreet(street);
        address.setHouse(house);
        address.setRoom(room);
        return address;
    }


    @Override
    public void download() {
        List<Map<String, Object>> allAddress = addressDao.findAll();

        BulkRequest bulkRequest = new BulkRequest();

        for (Map<String, Object> row : allAddress) {
            try {
                String city = row.get("CityType") != null ? row.get("CityType").toString() : "";
                String cityValue = row.get("City") != null ? row.get("City").toString() : "";
                String street = row.get("StreetType") != null ? row.get("StreetType").toString() : "";
                String streetValue = row.get("Street") != null ? row.get("Street").toString() : "";
                String house = row.get("ShortNameHouse") != null ? row.get("ShortNameHouse").toString() : "";
                String houseValue = row.get("House") != null ? row.get("House").toString() : "";
                String room = row.get("ShortNameRoom") != null ? row.get("ShortNameRoom").toString() : "";
                String roomValue = row.get("Room") != null ? row.get("Room").toString() : "";

                Address address = new Address();
                address.setCity(city +" "+ cityValue);
                address.setStreet(street +" "+ streetValue);
                address.setHouse(house +" "+ houseValue);
                address.setRoom(room +" "+ roomValue);

                IndexRequest indexRequest = new IndexRequest(INDEX_NAME);
                indexRequest.id(UUID.randomUUID().toString());
                indexRequest.source(mapper.writeValueAsString(address), XContentType.JSON);

                bulkRequest.add(indexRequest);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        try {
            BulkResponse bulkResponse = esClient.bulk(bulkRequest, RequestOptions.DEFAULT);

            if (bulkResponse.hasFailures()) {
                for (BulkItemResponse bulkItemResponse : bulkResponse) {
                    if (bulkItemResponse.isFailed()) {
                        BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                        throw new RuntimeException("Failed to process request: " + failure.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

