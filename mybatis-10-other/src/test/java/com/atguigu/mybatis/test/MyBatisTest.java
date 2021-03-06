package com.atguigu.mybatis.test;

import com.atguigu.mybatis.bean.EmpStatus;
import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.bean.OraclePage;
import com.atguigu.mybatis.dao.EmployeeMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class MyBatisTest {

    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
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
            Page<Object> page = PageHelper.startPage(5, 1);

            List<Employee> emps = mapper.getEmps();
            //传入要连续显示多少页
            PageInfo<Employee> info = new PageInfo<>(emps, 5);
            emps.forEach(System.out::println);
            System.out.println("当前页码：" + page.getPageNum());
            System.out.println("总记录数：" + page.getTotal());
            System.out.println("每页的记录数：" + page.getPageSize());
            System.out.println("总页码：" + page.getPages());

            System.out.println("当前页码：" + info.getPageNum());
            System.out.println("总记录数：" + info.getTotal());
            System.out.println("每页的记录数：" + info.getPageSize());
            System.out.println("总页码：" + info.getPages());
            System.out.println("是否第一页：" + info.isIsFirstPage());
            System.out.println("是否最后一页：" + info.isIsLastPage());
            System.out.println("连续显示的页码：");
            int[] nums = info.getNavigatepageNums();
            for (int i = 0; i < nums.length; i++) {
                System.out.println(nums[i]);
            }
        } finally {
            openSession.close();
        }
    }

    @Test
    public void testBatch() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //可以执行批量操作的SqlSession
        SqlSession openSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        long start = System.currentTimeMillis();
        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            for (int i = 0; i < 10000; i++) {
                mapper.addEmp(new Employee(UUID.randomUUID().toString().substring(0, 5), "b", "1"));
            }
            openSession.commit();
            long end = System.currentTimeMillis();
            //批量：预编译SQL 1次==>设置参数 10000次==>执行 1次，用时：2092
            //非批量：预编译SQL 10000次==>设置参数 10000次==>执行 10000次，用时：3944
            System.out.println("执行时长：" + (end - start));
        } finally {
            openSession.close();
        }
    }

    /**
     * Oracle分页：借助行号和子查询，存储过程包装分页逻辑
     */
    @Test
    public void testProcedure() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            OraclePage page = new OraclePage();
            page.setStart(1);
            page.setEnd(5);
            mapper.getPageByProcedure(page);

            System.out.println("总记录数：" + page.getCount());
            System.out.println("查出的数据大小：" + page.getEmps().size());
            System.out.println("查出的数据：" + page.getEmps());
        } finally {
            openSession.close();
        }
    }

    @Test
    public void testEnumUse() {
        EmpStatus login = EmpStatus.LOGIN;
        System.out.println("枚举的索引：" + login.ordinal());
        System.out.println("枚举的名字：" + login.name());

        System.out.println("枚举的状态码：" + login.getCode());
        System.out.println("枚举的提示消息：" + login.getMsg());
    }

    /**
     * MyBatis默认使用EnumTypeHandler处理枚举对象，保存的是枚举的名字；还可以使用EnumOrdinalTypeHandler
     */
    @Test
    public void testEnum() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            Employee employee = new Employee("test_enum", "enum@atguigu.com", "1");
            mapper.addEmp(employee);
            System.out.println("保存成功" + employee.getId());
            openSession.commit();
            Employee empById = mapper.getEmpById(10013);
            System.out.println(empById.getEmpStatus());
        } finally {
            openSession.close();
        }
    }

}
