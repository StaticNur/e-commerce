package com.staticnur.services;

import com.staticnur.model.Address;

import java.util.List;

public interface EsService {

    List<Address> searchAddress(String query);

    void download();
}
