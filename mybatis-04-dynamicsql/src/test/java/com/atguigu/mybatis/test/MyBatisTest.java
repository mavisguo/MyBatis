package com.atguigu.mybatis.test;

import com.atguigu.mybatis.bean.Department;
import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.dao.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MyBatisTest {

    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void testInnerParam() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            Employee employee2 = new Employee();
            employee2.setLastName("%e%");
            List<Employee> list = mapper.getEmpsTestInnerParameter(employee2);
            for (Employee employee : list) {
                System.out.println(employee);
            }
        } finally {
            openSession.close();
        }
    }

    @Test
    public void testBatchSave() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            List<Employee> emps = new ArrayList<>();
            emps.add(new Employee(null, "smith0x1", "smith0x1@atguigu.com", "1", new Department(1)));
            emps.add(new Employee(null, "allen0x1", "allen0x1@atguigu.com", "0", new Department(1)));
            mapper.addEmps(emps);
            openSession.commit();
        } finally {
            openSession.close();
        }
    }

    @Test
    public void testDynamicSQL() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            Employee employee = new Employee(1, "admin", null, null);

            //测试if/where
            List<Employee> emps = mapper.getEmpsByConditionIf(employee);
            emps.forEach(System.out::println);

            /*
             * 查询的时候如果某些条件没带可能会导致SQL拼装有问题，解决方案：
             * 1.给where后面加上1=1，以后的条件前面都加and；
             * 2.使用where标签将所有查询条件包括在内，MyBatis会将where标签中拼装的SQL多出来的and或or去掉，但where标签只会去掉第一个多出来的and或or。
             */

            //测试trim
            List<Employee> emps2 = mapper.getEmpsByConditionTrim(employee);
            emps2.forEach(System.out::println);

            //测试choose
            List<Employee> emps3 = mapper.getEmpsByConditionChoose(employee);
            emps3.forEach(System.out::println);

            //测试set
            mapper.updateEmp(employee);
            openSession.commit();

            List<Employee> emps4 = mapper.getEmpsByConditionForeach(Arrays.asList(1, 2));
            emps4.forEach(System.out::println);
        } finally {
            openSession.close();
        }
    }

    @Test
    public void test() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            Employee employee = openSession.selectOne("com.atguigu.mybatis.EmployeeMapper.selectEmp", 1);
            System.out.println(employee);
        } finally {
            openSession.close();
        }
    }

    @Test
    public void test01() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmpById(1);
            System.out.println(mapper.getClass());
            System.out.println(employee);
        } finally {
            openSession.close();
        }
    }

    @Test
    public void test02() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapperAnnotation mapper = openSession.getMapper(EmployeeMapperAnnotation.class);
            Employee empById = mapper.getEmpById(1);
            System.out.println(empById);
        } finally {
            openSession.close();
        }
    }

    @Test
    public void test03() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);

            Employee employee = new Employee(2, "jerry", "jerry@atguigu.com", "1");
            mapper.addEmp(employee);
            System.out.println(employee.getId());

//            Employee employee = new Employee(2, "jerry", "jerry@atguigu.com", "0");
//            boolean updateEmp = mapper.updateEmp(employee);
//            System.out.println(updateEmp);

//            mapper.deleteEmpById(2);

            openSession.commit();
        } finally {
            openSession.close();
        }
    }

    @Test
    public void test04() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);

//            Employee employee = mapper.getEmpByIdAndLastName(1, "tom");
//            System.out.println(employee);

//            Map<String, Object> map = new HashMap<>();
//            map.put("id", 1);
//            map.put("lastName", "Tom");
//            map.put("tableName", "tbl_employee");
//            Employee employee = mapper.getEmpByMap(map);
//            System.out.println(employee);

//            List<Employee> employees = mapper.getEmpsByLastNameLike("%e%");
//            employees.forEach(System.out::println);

//            Map<String, Object> map = mapper.getEmpByIdReturnMap(1);
//            System.out.println(map);

            Map<String, Employee> map = mapper.getEmpByLastNameLikeReturnMap("%r%");
            System.out.println(map);
        } finally {
            openSession.close();
        }
    }

    @Test
    public void test05() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapperPlus mapper = openSession.getMapper(EmployeeMapperPlus.class);

//            Employee empById = mapper.getEmpById(1);
//            System.out.println(empById);

//            Employee empAndDept = mapper.getEmpAndDept(1);
//            System.out.println(empAndDept);
//            System.out.println(empAndDept.getDept());

            Employee empByIdStep = mapper.getEmpByIdStep(1);
            System.out.println(empByIdStep.getLastName());
            System.out.println(empByIdStep.getDept());
        } finally {
            openSession.close();
        }
    }

    @Test
    public void test06() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            DepartmentMapper mapper = openSession.getMapper(DepartmentMapper.class);

//            Department department = mapper.getDeptByIdPlus(1);
//            System.out.println(department);
//            System.out.println(department.getEmps());

            Department deptByIdStep = mapper.getDeptByIdStep(1);
            System.out.println(deptByIdStep.getDepartmentName());
            System.out.println(deptByIdStep.getEmps());
        } finally {
            openSession.close();
        }
    }

}
