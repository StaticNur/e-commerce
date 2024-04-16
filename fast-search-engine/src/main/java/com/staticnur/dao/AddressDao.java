package com.staticnur.dao;

import com.staticnur.model.Address;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public interface AddressDao {
    List<Map<String, Object>> findAll();
}
