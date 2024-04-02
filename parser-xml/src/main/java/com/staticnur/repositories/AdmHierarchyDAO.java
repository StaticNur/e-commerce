package com.staticnur.repositories;

import com.staticnur.models.Attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AdmHierarchyDAO implements DataDAO {
    private JdbcTemplate jdbcTemplate;

    public void save(List<Attribute> data) {
        String INSERT_QUERY = "INSERT INTO RUSSIAN_ADDRESS_DATA.ADM_HIERARCHY (id, object_id, parent_obj_id, change_id, region_code, area_code, city_code, place_code, plan_code, street_code, prev_id, next_id, update_date, start_date, end_date, is_active, path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        //jdbcTemplate.batchUpdate(INSERT_QUERY, data, new int[] {Types.BIGINT, Types.BIGINT, Types.BIGINT, Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.BIGINT, Types.BIGINT, Types.DATE, Types.DATE, Types.DATE, Types.INTEGER, Types.VARCHAR});
        jdbcTemplate.batchUpdate(INSERT_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Attribute row = data.get(i);
                Long prevId = row.getAttributesByKey("PREVID").equals("") ? null : Long.parseLong(row.getAttributesByKey("PREVID"));
                Long nextId = row.getAttributesByKey("NEXTID").equals("") ? null : Long.parseLong(row.getAttributesByKey("NEXTID"));
                Long parentObjId = row.getAttributesByKey("PARENTOBJID").equals("") ? null : Long.parseLong(row.getAttributesByKey("PARENTOBJID"));

                ps.setLong(1, Long.parseLong(row.getAttributesByKey("ID")));
                ps.setLong(2, Long.parseLong(row.getAttributesByKey("OBJECTID")));
                ps.setObject(3, parentObjId);
                ps.setLong(4, Long.parseLong(row.getAttributesByKey("CHANGEID")));
                ps.setString(5, row.getAttributesByKey("REGIONCODE"));
                ps.setString(6, row.getAttributesByKey("AREACODE"));
                ps.setString(7, row.getAttributesByKey("CITYCODE"));
                ps.setString(8, row.getAttributesByKey("PLACECODE"));
                ps.setString(9, row.getAttributesByKey("PLANCODE"));
                ps.setString(10, row.getAttributesByKey("STREETCODE"));
                ps.setObject(11, prevId);
                ps.setObject(12, nextId);
                ps.setDate(13, Date.valueOf(row.getAttributesByKey("UPDATEDATE")));
                ps.setDate(14, Date.valueOf(row.getAttributesByKey("STARTDATE")));
                ps.setDate(15, Date.valueOf(row.getAttributesByKey("ENDDATE")));
                ps.setInt(16, Integer.parseInt(row.getAttributesByKey("ISACTIVE")));
                ps.setString(17, row.getAttributesByKey("PATH"));
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
