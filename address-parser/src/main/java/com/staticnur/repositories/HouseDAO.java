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
public class HouseDAO implements DataDAO {
    private JdbcTemplate jdbcTemplate;
@Override
    public void save(List<Attribute> data) {
        String INSERT_QUERY = "INSERT INTO RUSSIAN_ADDRESS_DATA.HOUSE (id, object_id, object_guid, change_id, house_num, addnum1, addnum2, house_type, addtype1, addtype2, oper_type_id, prev_id, next_id, update_date, start_date, end_date, is_actual, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(INSERT_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Attribute row = data.get(i);
                Long prevId = row.getAttributesByKey("PREVID").equals("") ? null : Long.parseLong(row.getAttributesByKey("PREVID"));
                Long nextId = row.getAttributesByKey("NEXTID").equals("") ? null : Long.parseLong(row.getAttributesByKey("NEXTID"));

                Integer addType1 = row.getAttributesByKey("ADDTYPE1").isEmpty() ? null : Integer.parseInt(row.getAttributesByKey("ADDTYPE1"));
                Integer addType2 = row.getAttributesByKey("ADDTYPE2").isEmpty() ? null : Integer.parseInt(row.getAttributesByKey("ADDTYPE2"));
                Integer houseType = row.getAttributesByKey("HOUSETYPE").isEmpty() ? null : Integer.parseInt(row.getAttributesByKey("HOUSETYPE"));

                ps.setLong(1, Long.parseLong(row.getAttributesByKey("ID")));
                ps.setLong(2, Long.parseLong(row.getAttributesByKey("OBJECTID")));
                ps.setString(3, row.getAttributesByKey("OBJECTGUID"));
                ps.setLong(4, Long.parseLong(row.getAttributesByKey("CHANGEID")));
                ps.setString(5, row.getAttributesByKey("HOUSENUM"));
                ps.setString(6, row.getAttributesByKey("ADDNUM1"));
                ps.setString(7, row.getAttributesByKey("ADDNUM2"));
                ps.setObject(8, houseType);
                ps.setObject(9, addType1);
                ps.setObject(10, addType2);
                ps.setLong(11, Long.parseLong(row.getAttributesByKey("OPERTYPEID")));
                ps.setObject(12, prevId);
                ps.setObject(13, nextId);
                ps.setDate(14, Date.valueOf(row.getAttributesByKey("UPDATEDATE")));
                ps.setDate(15, Date.valueOf(row.getAttributesByKey("STARTDATE")));
                ps.setDate(16, Date.valueOf(row.getAttributesByKey("ENDDATE")));
                ps.setInt(17, Integer.parseInt(row.getAttributesByKey("ISACTUAL")));
                ps.setInt(18, Integer.parseInt(row.getAttributesByKey("ISACTIVE")));
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
