package com.atguigu.mybatis.dao;

import com.atguigu.mybatis.bean.Employee;

import java.util.List;

public interface EmployeeMapper {

    Employee getEmpById(Integer id);

    List<Employee> getEmps();

}
