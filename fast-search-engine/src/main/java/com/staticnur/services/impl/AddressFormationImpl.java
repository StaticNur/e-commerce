package com.staticnur.services.impl;

import com.staticnur.dao.AddressDao;
import com.staticnur.mapper.Mapper;
import com.staticnur.model.Address;
import com.staticnur.services.AddressFormation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddressFormationImpl implements AddressFormation {
    private final AddressDao addressDao;
    private final Mapper mapper;

    @Autowired
    public AddressFormationImpl(AddressDao addressDao, Mapper mapper) {
        this.addressDao = addressDao;
        this.mapper = mapper;
    }

    @Override
    public List<Address> getAddress(long fromIndex, long toIndex) {
        List<Map<String, Object>> addressBatch = addressDao.findBatch(fromIndex, toIndex);
        /*for (Map<String, Object> address : addressBatch) {
            String[] path = address.get("path").toString().split("\\.");
            if (path.length == 6) {
                setApartmentInMap(address, path, path.length - 1);
            }else if (path.length == 7) {
                setApartmentInMap(address, path, path.length - 2);
                Map<String, Object> apartment = addressDao.findRoom(Integer.parseInt(path[path.length - 1]));
                address.put("short_name_room", apartment.get("short_name_room"));
                address.put("room_num", apartment.get("room_num"));
            }
        }*/
        List<Address> addressList = new ArrayList<>();
        for (Map<String, Object> address : addressBatch) {
            addressList.add(mapper.buildAddressFromMap(address));
        }
        return addressList;
    }

    /*private void setApartmentInMap(Map<String, Object> address, String[] path, int indexPath){
        Map<String, Object> apartment = addressDao.findApartment(Integer.parseInt(path[indexPath]));
        address.put("apartment_short_name", apartment.get("apartment_short_name"));
        address.put("apartment_num", apartment.get("apartment_num"));
    }*/
}
