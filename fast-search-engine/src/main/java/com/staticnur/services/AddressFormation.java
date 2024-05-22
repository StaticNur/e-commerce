package com.staticnur.services;

import com.staticnur.model.Address;

import java.util.List;
import java.util.Map;

public interface AddressFormation {
    List<Address> getAddress(long fromIndex, long toIndex);
}
