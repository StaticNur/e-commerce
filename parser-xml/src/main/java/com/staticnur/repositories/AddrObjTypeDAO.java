package com.staticnur.repositories;

import com.staticnur.models.Attribute;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository
public class AddrObjTypeDAO implements DataDAO {
    private JdbcTemplate jdbcTemplate;



    public void save(List<Attribute> data) {
        String INSERT_QUERY = "INSERT INTO RUSSIAN_ADDRESS_DATA.ADDR_OBJ_TYPE (id, level, name, short_name, description, update_date, start_date, end_date, is_active) VALUES(?,?,?,?,?,?,?,?,?)";
        //jdbcTemplate.batchUpdate(INSERT_QUERY, data, new int[] {Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.DATE, Types.DATE, Types.DATE, Types.BOOLEAN});
        jdbcTemplate.batchUpdate(INSERT_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Attribute row = data.get(i);
                ps.setLong(1, Long.parseLong(row.getAttributesByKey("ID")));
                ps.setInt(2, Integer.parseInt(row.getAttributesByKey("LEVEL")));
                ps.setString(3, row.getAttributesByKey("NAME"));
                ps.setString(4, row.getAttributesByKey("SHORTNAME"));
                ps.setString(5, row.getAttributesByKey("DESCRIPTION"));
                ps.setDate(6, Date.valueOf(row.getAttributesByKey("UPDATEDATE")));
                ps.setDate(7, Date.valueOf(row.getAttributesByKey("STARTDATE")));
                ps.setDate(8, Date.valueOf(row.getAttributesByKey("ENDDATE")));
                ps.setBoolean(9, Boolean.parseBoolean(row.getAttributesByKey("ISACTIVE")));
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

