package com.atguigu.mybatis.service;

import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.dao.EmployeeMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private SqlSession sqlSession;

    public List<Employee> getEmps() {
        List<Employee> emps = employeeMapper.getEmps();

//        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
//        List<Employee> emps = mapper.getEmps();

        return emps;
    }

}
