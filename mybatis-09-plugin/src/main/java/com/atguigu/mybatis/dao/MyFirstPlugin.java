package com.atguigu.mybatis.dao;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.util.Properties;

//完成插件签名：告诉MyBatis当前插件用来拦截哪个对象的哪个方法
@Intercepts({@Signature(type = StatementHandler.class, method = "parameterize", args = java.sql.Statement.class)})
public class MyFirstPlugin implements Interceptor {

    /**
     * 拦截目标方法
     *
     * @param invocation 目标方法
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("MyFirstPlugin...intercept()...拦截的方法：" + invocation.getMethod());
        //动态地改变一下SQL运行的参数：本来要查询1号员工，实际上从数据库查询3号员工
        Object target = invocation.getTarget();
        System.out.println("当前拦截到的对象：" + target);
        MetaObject metaObject = SystemMetaObject.forObject(target);
        Object value = metaObject.getValue("parameterHandler.parameterObject");
        System.out.println("SQL语句要用的参数：" + value);
        //修改SQL语句要用的参数
        metaObject.setValue("parameterHandler.parameterObject", 3);
        //执行目标方法
        Object proceed = invocation.proceed();
        //返回执行后的返回值
        return proceed;
    }

    /**
     * 包装目标对象，为目标对象创建一个代理对象
     *
     * @param target 目标对象
     * @return
     */
    @Override
    public Object plugin(Object target) {
        System.out.println("MyFirstPlugin...plugin()...包装的对象：" + target);
        //我们可以借助Plugin的wrap方法来使用当前Interceptor包装目标对象
        Object wrap = Plugin.wrap(target, this);
        //返回为当前目标对象创建的代理对象
        return wrap;
    }

    /**
     * 将插件注册时的属性设置进来
     *
     * @param properties 插件注册时的属性
     */
    @Override
    public void setProperties(Properties properties) {
        System.out.println("插件的配置信息：" + properties);
    }

}
