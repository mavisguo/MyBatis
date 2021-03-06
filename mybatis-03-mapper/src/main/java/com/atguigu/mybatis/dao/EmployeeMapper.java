package com.atguigu.mybatis.dao;

import com.atguigu.mybatis.bean.Employee;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EmployeeMapper {

    //多条记录封装一个Map：key是这条记录的主键，value是记录封装后的JavaBean
    @MapKey("lastName")
//告诉MyBatis封装这个Map的时候使用哪个属性作为Map的key
    Map<String, Employee> getEmpByLastNameLikeReturnMap(String lastName);

    //返回一条记录的Map：key就是列名，value就是列值
    Map<String, Object> getEmpByIdReturnMap(Integer id);

    List<Employee> getEmpsByLastNameLike(String lastName);

    Employee getEmpByMap(Map<String, Object> map);

    Employee getEmpByIdAndLastName(@Param("id") Integer id, @Param("lastName") String lastName);

    Employee getEmpById(Integer id);

    Long addEmp(Employee employee);

    boolean updateEmp(Employee employee);

    void deleteEmpById(Integer id);

}
