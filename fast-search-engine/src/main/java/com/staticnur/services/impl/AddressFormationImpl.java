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
        List<Address> addressList = new ArrayList<>();
        for (Map<String, Object> address : addressBatch) {
            addressList.add(mapper.buildAddressFromMap(address));
        }
        return addressList;
    }
}
