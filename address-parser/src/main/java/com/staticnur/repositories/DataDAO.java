package com.staticnur.repositories;

import com.staticnur.models.Attribute;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public interface DataDAO {
    void save(List<Attribute> data);

    void setJdbcTemplate(JdbcTemplate jdbcTemplate);
}
