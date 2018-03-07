package com.epam.brest.course.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-db-spring.xml", "classpath:test-dao.xml"})
@Rollback
@Transactional(transactionManager = "transactionManager")
public class EmployeeDaoImplTest {

    @Test
    public void getEmployees() throws Exception {
    }

    @Test
    public void getEmployeesByDepartmentId() throws Exception {
    }

    @Test
    public void getEmployeeById() throws Exception {
    }

    @Test
    public void addEmployee() throws Exception {
    }

    @Test
    public void updateEmployee() throws Exception {
    }

    @Test
    public void deleteEmployeeById() throws Exception {
    }

}