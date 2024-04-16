package com.staticnur.repositories;

import com.staticnur.models.Attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataHandler {
    private final JdbcTemplate jdbcTemplate;
    private static final Map<String, DataDAO> daoMap = new HashMap<>() {{
        put("AS_ADDR_OBJ", new AddrObjDAO());
        put("AS_ADDR_OBJ_TYPES", new AddrObjTypeDAO());
        put("AS_ADM_HIERARCHY", new AdmHierarchyDAO());
        put("AS_APARTMENTS", new ApartmentDAO());
        put("AS_APARTMENT_TYPES", new ApartmentTypeDAO());
        put("AS_HOUSES", new HouseDAO());
        put("AS_HOUSE_TYPES", new HouseTypeDAO());
        put("AS_ROOMS", new RoomDAO());
        put("AS_ROOM_TYPES", new RoomTypeDAO());
    }};

    @Autowired
    public DataHandler(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void assignMethodToData(String nameFile, List<Attribute> data) {
        DataDAO dataDAO = daoMap.get(nameFile);
        if (dataDAO != null) {
            dataDAO.setJdbcTemplate(jdbcTemplate);
            dataDAO.save(data);
        }
    }
}
