package com.atguigu.mybatis.dao;

import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.bean.OraclePage;

import java.util.List;

public interface EmployeeMapper {

    Employee getEmpById(Integer id);

    List<Employee> getEmps();

    Long addEmp(Employee employee);

    void getPageByProcedure(OraclePage page);

}
