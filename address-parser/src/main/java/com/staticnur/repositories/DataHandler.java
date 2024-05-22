package com.staticnur.repositories;

import com.staticnur.models.Attribute;
import com.staticnur.services.impl.FileServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataHandler {
    private static final Logger log = LoggerFactory.getLogger(DataHandler.class);
    private final JdbcTemplate jdbcTemplate;
    private final Map<String, DataDAO> daoMap;

    @Autowired
    public DataHandler(JdbcTemplate jdbcTemplate,
                       AddrObjDAO addrObjDAO,
                       AddrObjTypeDAO addrObjTypeDAO,
                       AdmHierarchyDAO admHierarchyDAO,
                       ApartmentDAO apartmentDAO,
                       ApartmentTypeDAO apartmentTypeDAO,
                       HouseDAO houseDAO,
                       HouseTypeDAO houseTypeDAO,
                       RoomDAO roomDAO,
                       RoomTypeDAO roomTypeDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.daoMap = new HashMap<>();
        daoMap.put("AS_ADDR_OBJ", addrObjDAO);
        daoMap.put("AS_ADDR_OBJ_TYPES", addrObjTypeDAO);
        daoMap.put("AS_ADM_HIERARCHY", admHierarchyDAO);
        daoMap.put("AS_APARTMENTS", apartmentDAO);
        daoMap.put("AS_APARTMENT_TYPES", apartmentTypeDAO);
        daoMap.put("AS_HOUSES", houseDAO);
        daoMap.put("AS_HOUSE_TYPES", houseTypeDAO);
        daoMap.put("AS_ROOMS", roomDAO);
        daoMap.put("AS_ROOM_TYPES", roomTypeDAO);
    }

    public void assignMethodToData(String nameFile, List<Attribute> data) {
        DataDAO dataDAO = daoMap.get(nameFile);
        if (dataDAO != null) {
            dataDAO.setJdbcTemplate(jdbcTemplate);
            try {
                dataDAO.save(data);
            } catch (Exception e) {
                log.error("Ошибка при вставке данных в PostgreSQL", e);
            }
        }
    }
}

