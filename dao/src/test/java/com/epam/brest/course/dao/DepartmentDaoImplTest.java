package com.epam.brest.course.dao;

import com.epam.brest.course.model.Department;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.core.StringStartsWith.startsWith;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-db-spring.xml", "classpath:test-dao.xml"})
@Rollback
@Transactional(transactionManager = "transactionManager")
public class DepartmentDaoImplTest {

    @Autowired
    DepartmentDao departmentDao;

    @Test
    public void getDepartments() {
        List<Department> departments = departmentDao.getDepartments();
        Assert.assertFalse(departments.isEmpty());
    }

    @Test
    public void getDepartmentById() {
        Integer sizeBefore1 = departmentDao.getDepartments().size();
        System.out.println(sizeBefore1);
        Department department = departmentDao.getDepartmentById(1);
        Assert.assertNotNull(department);
        Assert.assertTrue(department.getDepartmentId().equals(1));
        Assert.assertTrue(department.getDepartmentName().equals("Distribution"));
        Assert.assertTrue(department.getDescription().equals("Distribution Department"));
        Integer sizeAfter1 = departmentDao.getDepartments().size();
        System.out.println(sizeAfter1);
    }

    @Test
    public void getDepartmentByIdWithBeanPropertyRowMapper() {
        Integer sizeBefore1 = departmentDao.getDepartments().size();
        System.out.println(sizeBefore1);
        Department department = departmentDao.getDepartmentByIdWithBeanPropertyRowMapper(1);
        Assert.assertNotNull(department);
        Assert.assertTrue(department.getDepartmentId().equals(1));
        Assert.assertTrue(department.getDepartmentName().equals("Distribution"));
        Assert.assertTrue(department.getDescription().equals("Distribution Department"));
        Integer sizeAfter1 = departmentDao.getDepartments().size();
        System.out.println(sizeAfter1);
    }


    @Test
    public void addDepartment() {
        Integer sizeBefore1 = departmentDao.getDepartments().size();
        System.out.println(sizeBefore1);
        Department department = new Department("Education and Training", "Department of Education and Training");
        Integer sizeBefore = departmentDao.getDepartments().size();
        Department returnedDepartment = departmentDao.addDepartment(department);
        Integer sizeAfter = departmentDao.getDepartments().size();
        Assert.assertTrue(sizeBefore < sizeAfter);
        Assert.assertTrue(returnedDepartment.getDepartmentId() != null);
        Department savedDepartment = departmentDao.getDepartmentById(returnedDepartment.getDepartmentId());
        Assert.assertTrue(savedDepartment.getDepartmentId().equals(returnedDepartment.getDepartmentId()));
        Assert.assertTrue(savedDepartment.getDepartmentName().equals(department.getDepartmentName()));
        Assert.assertTrue(savedDepartment.getDescription().equals(department.getDescription()));
        Integer sizeAfter1 = departmentDao.getDepartments().size();
        System.out.println(sizeAfter1);
    }

    @Test
    public void addDepartmentWithSimpleJdbcInsert() {
        Integer sizeBefore1 = departmentDao.getDepartments().size();
        System.out.println(sizeBefore1);
        Department department = new Department("Education and Training", "Department of Education and Training");
        Integer sizeBefore = departmentDao.getDepartments().size();
        Department returnedDepartment = departmentDao.addDepartmentWithSimpleJdbcInsert(department);
        Integer sizeAfter = departmentDao.getDepartments().size();
        Assert.assertTrue(sizeBefore < sizeAfter);
        Assert.assertTrue(returnedDepartment.getDepartmentId() != null);
        Department savedDepartment = departmentDao.getDepartmentById(returnedDepartment.getDepartmentId());
        Assert.assertTrue(savedDepartment.getDepartmentId().equals(returnedDepartment.getDepartmentId()));
        Assert.assertTrue(savedDepartment.getDepartmentName().equals(department.getDepartmentName()));
        Assert.assertTrue(savedDepartment.getDescription().equals(department.getDescription()));
        Integer sizeAfter1 = departmentDao.getDepartments().size();
        System.out.println(sizeAfter1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDepartmentWithTheSameName() {
        Department department = new Department("Healthcare and Family Services",
                "Department of Healthcare and Family Services");
        departmentDao.addDepartment(department);
        departmentDao.addDepartment(department);
    }

    @Test
    public void updateDepartment() {
        Integer sizeBefore = departmentDao.getDepartments().size();
        System.out.println(sizeBefore);
        Department department = new Department("Education and Training", "Department of Education and Training");
        Department returnedDepartment = departmentDao.addDepartment(department);
        returnedDepartment.setDepartmentName("NEW Education and Training");
        returnedDepartment.setDescription("NEW Department of Education and Training");
        departmentDao.updateDepartment(returnedDepartment);
        Department updatedDepartment = departmentDao.getDepartmentById(returnedDepartment.getDepartmentId());
        Assert.assertTrue(updatedDepartment.getDepartmentId().equals(returnedDepartment.getDepartmentId()));
        Assert.assertTrue(updatedDepartment.getDepartmentName().equals(returnedDepartment.getDepartmentName()));
        Assert.assertTrue(updatedDepartment.getDescription().equals(returnedDepartment.getDescription()));
        Integer sizeAfter = departmentDao.getDepartments().size();
        System.out.println(sizeAfter);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteDepartmentById() {
        Integer sizeBefore1 = departmentDao.getDepartments().size();
        System.out.println(sizeBefore1);
        Integer sizeBefore = departmentDao.getDepartments().size();
        List<Department> departments = departmentDao.getDepartments();
        Assert.assertTrue(departments.size() > 0);
        Integer departmentId = departments.get(0).getDepartmentId();
        Integer deleteResult = departmentDao.deleteDepartmentById(departmentId);
        Assert.assertTrue(deleteResult > 0);
        Integer sizeAfter = departmentDao.getDepartments().size();
        Assert.assertTrue(sizeBefore > sizeAfter);
        departmentDao.getDepartmentById(departmentId);
        Integer sizeAfter1 = departmentDao.getDepartments().size();
        System.out.println(sizeAfter1);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void deleteDepartmentByIdWithRule() {
        Integer sizeBefore1 = departmentDao.getDepartments().size();
        System.out.println(sizeBefore1);
        Integer sizeBefore = departmentDao.getDepartments().size();
        List<Department> departments = departmentDao.getDepartments();
        Assert.assertTrue(departments.size() > 0);
        Integer departmentId = departments.get(0).getDepartmentId();

        thrown.expect(EmptyResultDataAccessException.class);
        thrown.expectMessage(startsWith("Incorrect result size:"));
        Integer deleteResult = departmentDao.deleteDepartmentById(departmentId);

        Assert.assertTrue(deleteResult > 0);
        Integer sizeAfter = departmentDao.getDepartments().size();
        Assert.assertTrue(sizeBefore > sizeAfter);
        departmentDao.getDepartmentById(departmentId);
        Integer sizeAfter1 = departmentDao.getDepartments().size();
        System.out.println(sizeAfter1);
    }
}