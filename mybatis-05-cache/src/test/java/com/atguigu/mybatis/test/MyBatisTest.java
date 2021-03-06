package com.atguigu.mybatis.test;

import com.atguigu.mybatis.bean.Department;
import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.dao.DepartmentMapper;
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
     * 两级缓存：
     * <p>
     * 一级缓存（本地缓存）：SqlSession级别的缓存，本质是一个Map，一级缓存是一直开启的。
     * 工作机制：与数据库同一次会话期间查询到的数据会放在本地缓存中。
     * 效果：以后如果需要获取相同的数据，直接从缓存中获取，没必要再去查询数据库。
     * 一级缓存失效情况，即没有使用到当前一级缓存的情况（效果就是还需要再向数据库发出查询）：
     * 1.SqlSession不同；
     * 2.SqlSession相同，查询条件不同（当前一级缓存中还没有这个数据）；
     * 3.SqlSession相同，两次查询之间执行了增删改操作（这次增删改可能对当前数据有影响）；
     * 4.SqlSession相同，手动清除了一级缓存（缓存清空）。
     * <p>
     * 二级缓存（全局缓存）：namespace级别的缓存，一个namespace对应一个二级缓存。
     * 工作机制：
     * 1.一个会话，查询一条数据，这个数据就会被放在当前会话的一级缓存中；
     * 2.如果会话关闭，一级缓存中的数据会被保存到二级缓存中；新的会话查询信息，就可以参照二级缓存中的内容；
     * 3.不同namespace查出的数据会放在自己对应的缓存中，本质也是Map；EmployeeMapper===>Employee，DepartmentMapper===>Department。
     * 效果：数据会从二级缓存中获取，查出的数据都会被默认先放在一级缓存中，只有会话提交或者关闭以后，一级缓存中的数据才会转移到二级缓存中。
     * 使用:
     * 1.开启全局二级缓存配置：<setting name="cacheEnabled" value="true"/>；
     * 2.去Mapper.xml中配置使用二级缓存：<cache/>；
     * 3.我们的POJO需要实现序列化接口。
     * <p>
     * 和缓存有关的属性设置：
     * 1.<setting name="cacheEnabled" value="true"/>。
     * cacheEnabled="true"：开启缓存；cacheEnabled="false"：缓存关闭（二级缓存关闭，一级缓存依然可用）。
     * 2.查询标签有useCache属性，默认为true。
     * useCache="true"：使用缓存；useCache="false"：不使用缓存（一级缓存依然使用，二级缓存不使用）。
     * 3-1.增删改标签有flushCache属性，默认为true。
     * flushCache="true"：每次增删改完成后，一级缓存和二级缓存都会被清除；flushCache="false"：每次增删改完成后，不清除缓存。
     * 3-2.查询标签也有flushCache属性，默认为false。
     * flushCache="false"：每次查询之后，不清除缓存；flushCache="true"：每次查询之后，都会清除缓存，实际上缓存是没有被使用的。
     * 4.sqlSession.clearCache();只是清除当前sqlSession的一级缓存。
     * 5.localCacheScope：一级缓存（本地缓存）作用域。
     * SESSION：当前会话的所有数据保存在会话缓存中；STATEMENT：禁用一级缓存。
     * <p>
     * 第三方缓存整合：
     * 1).导入第三方缓存包即可；
     * 2).导入与第三方缓存整合的适配包；
     * 3).Mapper.xml中使用自定义缓存。
     * <cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
     */

    @Test
    public void testSecondLevelCache02() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        SqlSession openSession2 = sqlSessionFactory.openSession();
        try {
            DepartmentMapper mapper = openSession.getMapper(DepartmentMapper.class);
            DepartmentMapper mapper2 = openSession2.getMapper(DepartmentMapper.class);

            Department deptById = mapper.getDeptById(1);
            System.out.println(deptById);
            openSession.close();

            Department deptById2 = mapper2.getDeptById(1);
            System.out.println(deptById2);
            openSession2.close();
        } finally {
        }
    }

    @Test
    public void testSecondLevelCache() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        SqlSession openSession2 = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            EmployeeMapper mapper2 = openSession2.getMapper(EmployeeMapper.class);

            Employee emp01 = mapper.getEmpById(1);
            System.out.println(emp01);
            openSession.close();

            //第二次查询是从二级缓存中拿到的数据，并没有发送新的SQL
            mapper2.addEmp(new Employee(null, "aaa", "nnn", "0"));
            Employee emp02 = mapper2.getEmpById(1);
            System.out.println(emp02);
            openSession2.close();
        } finally {
        }
    }

    @Test
    public void testFirstLevelCache() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            Employee emp01 = mapper.getEmpById(1);
            System.out.println(emp01);

            //一级缓存失效的情况
            //1.SqlSession不同
//            SqlSession openSession2 = sqlSessionFactory.openSession();
//            EmployeeMapper mapper2 = openSession2.getMapper(EmployeeMapper.class);
//            Employee emp02 = mapper2.getEmpById(1);

            //2.SqlSession相同，查询条件不同
//            Employee emp02 = mapper.getEmpById(3);

            //3.SqlSession相同，两次查询之间执行了增删改操作（这次增删改可能对当前数据有影响）
//            mapper.addEmp(new Employee(null, "testCache", "cache", "1"));
//            System.out.println("数据添加成功");
//            Employee emp02 = mapper.getEmpById(1);

            //4.SqlSession相同，手动清除了一级缓存
//            openSession.clearCache();
            Employee emp02 = mapper.getEmpById(1);

            System.out.println(emp02);
            System.out.println(emp01 == emp02);
        } finally {
            openSession.close();
        }
    }

}
