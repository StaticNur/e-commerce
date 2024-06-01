package com.staticnur.repositories;

import com.staticnur.models.Attribute;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RoomTypeDAO implements DataDAO {
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(List<Attribute> data) {
        String INSERT_QUERY = "INSERT INTO RUSSIAN_ADDRESS_DATA.ROOM_TYPE (id, name, short_name, description, is_active, start_date, end_date, update_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(INSERT_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Attribute row = data.get(i);
                ps.setInt(1, Integer.parseInt(row.getAttributesByKey("ID")));
                ps.setString(2, row.getAttributesByKey("NAME"));
                ps.setString(3, row.getAttributesByKey("SHORTNAME"));
                ps.setString(4, row.getAttributesByKey("DESCRIPTION"));
                ps.setBoolean(5, Boolean.parseBoolean(row.getAttributesByKey("ISACTIVE")));
                ps.setDate(6, Date.valueOf(row.getAttributesByKey("STARTDATE")));
                ps.setDate(7, Date.valueOf(row.getAttributesByKey("ENDDATE")));
                ps.setDate(8, Date.valueOf(row.getAttributesByKey("UPDATEDATE")));
            }

            @Override
            public int getBatchSize() {
                return data.size();
            }
        });
    }
    @Override
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
