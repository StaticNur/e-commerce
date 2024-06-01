package com.staticnur.mapper;

import com.staticnur.model.Address;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Mapper {

    public Address buildAddressFromMap(Map<String, Object> address) {
        String region_type = address.get("region_type") == null ? "" : address.get("region_type").toString();
        String region = address.get("region") == null ? "" : " " + address.get("region").toString();
        String district_type = address.get("district_type") == null ? "" : ", " + address.get("district_type").toString();
        String district = address.get("district") == null ? "" : " " + address.get("district").toString();

        String city_type = address.get("city_type") == null ? "" : ", " + address.get("city_type").toString();
        String city = address.get("city") == null ? "" : " " + address.get("city").toString();
        String street_type = address.get("street_type") == null ? "" : ", " + address.get("street_type").toString();
        String street = address.get("street") == null ? "" : " " + address.get("street").toString();

        String short_name_house = address.get("short_name_house") == null ? "" : ", " + address.get("short_name_house").toString();
        String house = address.get("house") == null ? "" : " " + address.get("house").toString();
        String short_name_apartm = address.get("short_name_apartm") == null ? "" : ", " + address.get("short_name_apartm").toString();
        String apartm = address.get("apartm") == null ? "" : " " + address.get("apartm").toString();

        String short_name_room = address.get("short_name_room") == null ? "" : ", " + address.get("short_name_room").toString();
        String room = address.get("room") == null ? "" : " " + address.get("room").toString();

        String addressStr = region_type + region + district_type + district +
                               city_type + city + street_type +  street +
                               short_name_house + house + short_name_apartm + apartm +
                               short_name_room + room;
        return Address.builder()
                .address(addressStr)
                .path(address.get("path").toString())
                .build();
    }

}
