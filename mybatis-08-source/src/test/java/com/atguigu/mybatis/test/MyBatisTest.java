package com.atguigu.mybatis.test;

import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisTest {

    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    /**
     * 1.获取SqlSessionFactory对象：解析配置文件的每一个信息保存在Configuration中，返回包含Configuration的DefaultSqlSessionFactory；
     * 2.获取SqlSession对象：使用DefaultSqlSessionFactory创建一个包含Executor和Configuration的DefaultSQlSession对象；
     * 3.获取接口的代理对象：使用MapperProxyFactory创建一个包含DefaultSqlSession的MapperProxy的代理对象；
     * 4.执行增删改查方法。
     * <p>
     * 总结:
     * 1.根据配置文件（全局配置文件和SQL映射文件）初始化出Configuration对象；
     * 2.创建一个DefaultSqlSession对象，里面包含Configuration以及Executor（根据全局配置文件中的defaultExecutorType创建的对应Executor）；
     * 3.DefaultSqlSession.getMapper()：拿到Mapper接口对应的MapperProxy，MapperProxy里面有DefaultSqlSession；
     * 4.执行增删改查方法：
     * 1).调用DefaultSqlSession的增删改查；
     * 2).会创建一个StatementHandler对象，同时也会创建出ParameterHandler和ResultSetHandler对象；
     * 3).调用StatementHandler的预编译参数方法，使用ParameterHandler给SQL设置参数；
     * 4).调用StatementHandler的增删改查方法，使用ResultSetHandler封装结果。
     */
    @Test
    public void test() throws IOException {
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

}
