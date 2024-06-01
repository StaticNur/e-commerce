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
public class RoomDAO implements DataDAO {
    private JdbcTemplate jdbcTemplate;
@Override
    public void save(List<Attribute> data) {
        String INSERT_QUERY = "INSERT INTO RUSSIAN_ADDRESS_DATA.ROOM (id, object_id, object_guid, change_id, number, room_type, oper_type_id, prev_id, next_id, update_date, start_date, end_date, is_actual, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(INSERT_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Attribute row = data.get(i);
                Long prevId = row.getAttributesByKey("PREVID").equals("") ? null : Long.parseLong(row.getAttributesByKey("PREVID"));
                Long nextId = row.getAttributesByKey("NEXTID").equals("") ? null : Long.parseLong(row.getAttributesByKey("NEXTID"));

                ps.setLong(1, Long.parseLong(row.getAttributesByKey("ID")));
                ps.setLong(2, Long.parseLong(row.getAttributesByKey("OBJECTID")));
                ps.setString(3, row.getAttributesByKey("OBJECTGUID"));
                ps.setLong(4, Long.parseLong(row.getAttributesByKey("CHANGEID")));
                ps.setString(5, row.getAttributesByKey("NUMBER"));
                ps.setInt(6, Integer.parseInt(row.getAttributesByKey("ROOMTYPE")));
                ps.setLong(7, Long.parseLong(row.getAttributesByKey("OPERTYPEID")));
                ps.setObject(8, prevId);
                ps.setObject(9, nextId);
                ps.setDate(10, Date.valueOf(row.getAttributesByKey("UPDATEDATE")));
                ps.setDate(11, Date.valueOf(row.getAttributesByKey("STARTDATE")));
                ps.setDate(12, Date.valueOf(row.getAttributesByKey("ENDDATE")));
                ps.setInt(13, Integer.parseInt(row.getAttributesByKey("ISACTUAL")));
                ps.setInt(14, Integer.parseInt(row.getAttributesByKey("ISACTIVE")));

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