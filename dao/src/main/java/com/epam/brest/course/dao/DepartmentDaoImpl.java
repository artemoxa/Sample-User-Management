package com.epam.brest.course.dao;

import com.epam.brest.course.model.Department;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DepartmentDaoImpl implements DepartmentDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${department.select}")
    private String departmentsSelect;

//    private final String GET_DEPARTMENTS_SQL = "SELECT departmentId, departmentName, description " +
//            "FROM department";

    private final String GET_DEPARTMENT_BY_ID_SQL = "SELECT departmentId, departmentName, description " +
            "FROM department WHERE departmentId = :departmentId";

    private final String CHECK_DEPARTMENT_NAME_SQL = "SELECT COUNT(departmentId) " +
            "FROM department WHERE lower(departmentName) = lower(:departmentName)";

    private final String INSERT_DEPARTMENT_SQL = "INSERT INTO department (departmentName, description) " +
            "VALUES (:departmentName, :description);";

    private final String UPDATE_DEPARTMENT_SQL = "UPDATE department SET departmentName = :departmentName, " +
            "description = :description WHERE departmentId = :departmentId";

    private final String DELETE_DEPARTMENT_SQL = "DELETE FROM department WHERE departmentId = ?";


    private DataSource dataSource;
    public DepartmentDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public DepartmentDaoImpl() {

    }

    @Override
    public List<Department> getDepartments() {
        List<Department> departments = namedParameterJdbcTemplate
                .getJdbcOperations().query(departmentsSelect, new DepartmentRowMapper());
        return departments;
    }

    @Override
    public Department getDepartmentById(Integer departmentId) {
        SqlParameterSource namedParameters =
                new MapSqlParameterSource("departmentId", departmentId);
        Department department =
                namedParameterJdbcTemplate.queryForObject(GET_DEPARTMENT_BY_ID_SQL, namedParameters,
                        new DepartmentRowMapper());
        return department;
    }

    public Department getDepartmentByIdWithBeanPropertyRowMapper(Integer departmentId) {
        SqlParameterSource namedParameters =
                new MapSqlParameterSource("departmentId", departmentId);
        Department department =
                namedParameterJdbcTemplate.queryForObject(GET_DEPARTMENT_BY_ID_SQL, namedParameters,
                        BeanPropertyRowMapper.newInstance(Department.class));
        return department;
    }

    @Override
    public Department addDepartment(Department department) {

        MapSqlParameterSource namedParameters =
                new MapSqlParameterSource("departmentName", department.getDepartmentName());
        Integer result = namedParameterJdbcTemplate.queryForObject(CHECK_DEPARTMENT_NAME_SQL, namedParameters, Integer.class);

        if (result == 0) {
            namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("departmentName", department.getDepartmentName());
            namedParameters.addValue("description", department.getDescription());

            KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
            namedParameterJdbcTemplate.update(INSERT_DEPARTMENT_SQL, namedParameters, generatedKeyHolder);
            department.setDepartmentId((Integer) generatedKeyHolder.getKey());
            return department;
        }

        throw new IllegalArgumentException("Department with the same name already exists in the database.");
    }

    /**
     * Add a new department by SimpleJdbcInsert.
     * @param department - new Department object with departmentId == null
     * @return new Department object with updated departmentId
     */
    @Override
    public Department addDepartmentWithSimpleJdbcInsert(Department department) {
        SimpleJdbcInsert insertActor = new SimpleJdbcInsert(dataSource).withTableName("department")
                .usingGeneratedKeyColumns("departmentId");
        Integer departmentId =
                (Integer) insertActor.executeAndReturnKey(new BeanPropertySqlParameterSource(department));
        department.setDepartmentId(departmentId);
        return department;
    }

    @Override
    public Integer updateDepartment(Department department) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(department);
        Integer result = namedParameterJdbcTemplate.update(UPDATE_DEPARTMENT_SQL, namedParameters);
        return result;
    }

    @Override
    public Integer deleteDepartmentById(Integer departmentId) {
        Integer result = namedParameterJdbcTemplate.getJdbcOperations().update(DELETE_DEPARTMENT_SQL, departmentId);
        return result;
    }

    private class DepartmentRowMapper implements RowMapper<Department> {

        @Override
        public Department mapRow(ResultSet resultSet, int i) throws SQLException {
            Department department = new Department();
            department.setDepartmentId(resultSet.getInt("departmentId"));
            department.setDepartmentName(resultSet.getString("departmentName"));
            department.setDescription(resultSet.getString("description"));
            return department;
        }
    }

}
