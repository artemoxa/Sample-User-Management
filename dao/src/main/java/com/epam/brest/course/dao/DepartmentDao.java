package com.epam.brest.course.dao;

import com.epam.brest.course.model.Department;

import java.util.List;

/**
 * Department DAO Interface.
 */
public interface DepartmentDao {

    List<Department> getDepartments();

    Department getDepartmentById(Integer departmentId);

    Department getDepartmentByIdWithBeanPropertyRowMapper(Integer departmentId);

    Department addDepartment(Department department);

    Department addDepartmentWithSimpleJdbcInsert(Department department);

    Integer updateDepartment(Department department);

    Integer deleteDepartmentById(Integer id);

}
