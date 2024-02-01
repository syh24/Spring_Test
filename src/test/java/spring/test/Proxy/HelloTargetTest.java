package spring.test.Proxy;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;

import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.*;

class HelloTargetTest {

    @Test
    void simpleProxy() {
        Hello hello = new HelloTarget();
        Assertions.assertThat(hello.sayHello("syh")).isEqualTo("Hello syh");
        Assertions.assertThat(hello.sayHi("syh")).isEqualTo("Hi syh");
        Assertions.assertThat(hello.sayThanks("syh")).isEqualTo("Thanks syh");

        //프록시 적용
        Hello proxy = new HelloUppercase(new HelloTarget());
        Assertions.assertThat(proxy.sayHello("syh")).isEqualTo("HELLO SYH");
        Assertions.assertThat(proxy.sayHi("syh")).isEqualTo("HI SYH");
        Assertions.assertThat(proxy.sayThanks("syh")).isEqualTo("THANKS SYH");
    }

    @Test
    //다이나믹 프록시 적용
    void dynamicProxy() {
        Hello dynamicProxy = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget())
        );
        Assertions.assertThat(dynamicProxy.sayHello("syh")).isEqualTo("HELLO SYH");
        Assertions.assertThat(dynamicProxy.sayHi("syh")).isEqualTo("HI SYH");
        Assertions.assertThat(dynamicProxy.sayThanks("syh")).isEqualTo("THANKS SYH");
    }

    @Test
    void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UpperCaseAdvice());

        Hello proxiedHello = (Hello) pfBean.getObject();

        Assertions.assertThat(proxiedHello.sayHello("syh")).isEqualTo("HELLO SYH");
        Assertions.assertThat(proxiedHello.sayHi("syh")).isEqualTo("HI SYH");
        Assertions.assertThat(proxiedHello.sayThanks("syh")).isEqualTo("THANKS SYH");
    }

    static class UpperCaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }
}