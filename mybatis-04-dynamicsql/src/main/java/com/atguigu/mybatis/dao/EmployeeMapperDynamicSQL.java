package com.atguigu.mybatis.dao;

import com.atguigu.mybatis.bean.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeMapperDynamicSQL {

    List<Employee> getEmpsTestInnerParameter(Employee employee);

    //携带了哪个字段，查询条件就带上这个字段的值
    List<Employee> getEmpsByConditionIf(Employee employee);

    List<Employee> getEmpsByConditionTrim(Employee employee);

    List<Employee> getEmpsByConditionChoose(Employee employee);

    void updateEmp(Employee employee);

    //查询id在给定集合中的员工
    List<Employee> getEmpsByConditionForeach(@Param("ids") List<Integer> ids);

    void addEmps(@Param("emps") List<Employee> emps);

}
