package com.staticnur.dao.impl;

import com.staticnur.dao.AddressDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AddressDaoImpl implements AddressDao {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public AddressDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> findBatch(long page, long size) {
        long offset = page * size;
        String GET_ALL_QUERY = """
                  SELECT region.type_name as region_type, region.name as region, district.type_name as district_type, district.name as district,
                   city.type_name as city_type, city.name as city, street.type_name as street_type, street.name as street,
                   ht.short_name as short_name_house, house.house_num as house, apt.short_name as short_name_apartm, apartm.number as apartm,
                   rt.short_name as short_name_room, room.number as room, i.path
                  FROM RUSSIAN_ADDRESS_DATA.adm_hierarchy i
                  LEFT JOIN RUSSIAN_ADDRESS_DATA.addr_obj region ON SPLIT_PART(i.path, '.', 1) = region.object_id::varchar
                  LEFT JOIN RUSSIAN_ADDRESS_DATA.addr_obj district ON SPLIT_PART(i.path, '.', 2) = district.object_id::varchar
                  LEFT JOIN RUSSIAN_ADDRESS_DATA.addr_obj city ON SPLIT_PART(i.path, '.', 3) = city.object_id::varchar
                  LEFT JOIN RUSSIAN_ADDRESS_DATA.addr_obj street ON SPLIT_PART(i.path, '.', 4) = street.object_id::varchar
                  LEFT JOIN RUSSIAN_ADDRESS_DATA.house house ON CASE
                          WHEN district.type_name = 'г' THEN SPLIT_PART(i.path, '.', 4)
                          ELSE SPLIT_PART(i.path, '.', 5)
                      END = house.object_id::varchar
                  LEFT JOIN RUSSIAN_ADDRESS_DATA.house_type ht ON ht.id = house.house_type
                  LEFT JOIN RUSSIAN_ADDRESS_DATA.apartment apartm ON CASE
                          WHEN district.type_name = 'г' THEN SPLIT_PART(i.path, '.', 5)
                          ELSE SPLIT_PART(i.path, '.', 6)
                      END = apartm.object_id::varchar
                  LEFT JOIN RUSSIAN_ADDRESS_DATA.apartment_type apt ON apt.id = apartm.apart_type
                  LEFT JOIN RUSSIAN_ADDRESS_DATA.room room ON CASE
                          WHEN district.type_name = 'г' THEN SPLIT_PART(i.path, '.', 6)
                          ELSE SPLIT_PART(i.path, '.', 7)
                      END = room.object_id::varchar
                  LEFT JOIN RUSSIAN_ADDRESS_DATA.room_type rt ON rt.id = room.room_type;
                """;
        return jdbcTemplate.queryForList(GET_ALL_QUERY);
    }
}