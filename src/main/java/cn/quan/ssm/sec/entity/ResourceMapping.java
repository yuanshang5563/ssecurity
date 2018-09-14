package cn.quan.ssm.sec.entity;

import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @auther zhangsq on 2017-9-6.
 */

public class ResourceMapping extends MappingSqlQuery {
    public ResourceMapping(DataSource dataSource, String resourceQuery) {
        super(dataSource,resourceQuery);
        compile();
    }

    /**
     * //对结果集进行封装处理
     * @param rs
     * @param i
     * @return
     * @throws SQLException
     */
    protected Object mapRow(ResultSet rs, int i) throws SQLException {
        String url = rs.getString(1);
        String role = rs.getString(2);
        Resource resource = new Resource(url,role);
        return resource;
    }
}
