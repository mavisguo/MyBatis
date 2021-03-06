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

    /**
     * 插件原理：
     * 1.在四大对象创建的时候，创建出来的对象不是直接返回的，而是调用interceptorChain.pluginAll(parameterHandler);方法；
     * 2.获取到所有的Interceptor，调用interceptor.plugin(target);方法；
     * 3.使用插件可以为目标对象创建代理对象。
     * <p>
     * 插件编写：
     * 1.编写Interceptor的实现类；
     * 2.使用@Intercepts注解完成插件签名；
     * 3.将写好的插件注册到全局配置文件中。
     */
    @Test
    public void testPlugin() {
    }

}
