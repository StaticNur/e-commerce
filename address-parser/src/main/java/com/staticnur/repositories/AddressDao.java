package com.staticnur.repositories;

import java.util.List;
import java.util.Map;

public interface AddressDao {
    List<Map<String, Object>> findAllAddress(int page, int size);
    void delete();
}

