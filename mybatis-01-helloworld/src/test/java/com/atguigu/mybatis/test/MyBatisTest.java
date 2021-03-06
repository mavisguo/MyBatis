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

/**
 * 1.接口式编程：
 * 原生：Dao ==> DaoImpl
 * MyBatis：Mapper ==> Mapper.xml
 * 2.SqlSession代表和数据库的一次会话，用完必须关闭。
 * 3.SqlSession和connection一样都是非线程安全的，每次使用都应该去获取新的对象。
 * 4.Mapper接口没有实现类，但是MyBatis会为这个接口生成一个代理对象。
 * 将接口和xml进行绑定：EmployeeMapper empMapper = sqlSession.getMapper(EmployeeMapper.class);
 * 5.两个重要的配置文件：
 * MyBatis的全局配置文件：包含数据库连接池信息，事务管理器信息等系统运行环境信息；
 * SQL映射文件：保存了每一个SQL语句的映射信息，将SQL抽取出来。
 */
public class MyBatisTest {

    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    /**
     * 1.根据全局配置文件创建一个SqlSessionFactory对象，有数据源等一些运行环境信息。
     * 2.SQL映射文件，配置了每一个SQL，以及SQL的封装规则等。
     * 3.将SQL映射文件注册在全局配置文件中。
     * 4.写代码：
     * 1).根据全局配置文件得到SqlSessionFactory；
     * 2).使用SqlSessionFactory，获取到SqlSession对象使用他来执行增删改查；一个SqlSession对象代表和数据库的一次会话，用完关闭；
     * 3).使用SQL的唯一标识来告诉MyBatis执行哪个SQL；SQL都是保存在SQL映射文件中的。
     */
    @Test
    public void test() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //获取SqlSession实例，能直接执行已经映射的SQL语句
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            //selectOne：第一个参数是SQL的唯一标识；第二个参数是执行SQL要用的参数
            Employee employee = openSession.selectOne("com.atguigu.mybatis.EmployeeMapper.selectEmp", 1);
            System.out.println(employee);
        } finally {
            openSession.close();
        }
    }

    //推荐使用这种接口式编程
    @Test
    public void test01() throws IOException {
        //1.获取SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //2.获取SqlSession对象
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            //3.获取接口的实现类对象，会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmpById(1);
            System.out.println(mapper.getClass());
            System.out.println(employee);
        } finally {
            openSession.close();
        }
    }

}
