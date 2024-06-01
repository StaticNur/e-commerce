package com.staticnur.dao;

import java.util.List;
import java.util.Map;

public interface AddressDao {
    List<Map<String, Object>> findBatch(long page, long size);
}
