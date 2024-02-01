package spring.test.Proxy;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class HelloTargetTest {

    @Test
    void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("syh")).isEqualTo("Hello syh");
        assertThat(hello.sayHi("syh")).isEqualTo("Hi syh");
        assertThat(hello.sayThanks("syh")).isEqualTo("Thanks syh");

        //프록시 적용
        Hello proxy = new HelloUppercase(new HelloTarget());
        assertThat(proxy.sayHello("syh")).isEqualTo("HELLO SYH");
        assertThat(proxy.sayHi("syh")).isEqualTo("HI SYH");
        assertThat(proxy.sayThanks("syh")).isEqualTo("THANKS SYH");
    }

    @Test
    //다이나믹 프록시 적용
    void dynamicProxy() {
        Hello dynamicProxy = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget())
        );
        assertThat(dynamicProxy.sayHello("syh")).isEqualTo("HELLO SYH");
        assertThat(dynamicProxy.sayHi("syh")).isEqualTo("HI SYH");
        assertThat(dynamicProxy.sayThanks("syh")).isEqualTo("THANKS SYH");
    }

    @Test
    void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UpperCaseAdvice());

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat(proxiedHello.sayHello("syh")).isEqualTo("HELLO SYH");
        assertThat(proxiedHello.sayHi("syh")).isEqualTo("HI SYH");
        assertThat(proxiedHello.sayThanks("syh")).isEqualTo("THANKS SYH");
    }

    @Test
    void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        Hello proxiedHello = (Hello) pfBean.getObject();

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UpperCaseAdvice()));
        assertThat(proxiedHello.sayHello("syh")).isEqualTo("HELLO SYH");
        assertThat(proxiedHello.sayHi("syh")).isEqualTo("HI SYH");
        assertThat(proxiedHello.sayThanks("syh")).isEqualTo("Thanks syh");
    }

    @Test
    void classNamePointcutAdvisor() {
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
            public ClassFilter getClassFilter() {
                return new ClassFilter() {
                    @Override
                    public boolean matches(Class<?> clazz) {
                        return clazz.getSimpleName().startsWith("HelloT");
                    }
                };
            }
        };

        classMethodPointcut.setMappedName("sayH*");

        checkAdviced(new HelloTarget(), classMethodPointcut, true);
        class HelloWorld extends HelloTarget {};
        checkAdviced(new HelloWorld(), classMethodPointcut, false);
        class HelloTT extends HelloTarget {};
        checkAdviced(new HelloTT(), classMethodPointcut, true);

    }

    private void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UpperCaseAdvice()));
        Hello proxiedHello = (Hello) pfBean.getObject();

        if (adviced) {
            assertThat(proxiedHello.sayHello("syh")).isEqualTo("HELLO SYH");
            assertThat(proxiedHello.sayHi("syh")).isEqualTo("HI SYH");
            assertThat(proxiedHello.sayThanks("syh")).isEqualTo("Thanks syh");
        } else {
            assertThat(proxiedHello.sayHello("syh")).isEqualTo("Hello syh");
            assertThat(proxiedHello.sayHi("syh")).isEqualTo("Hi syh");
            assertThat(proxiedHello.sayThanks("syh")).isEqualTo("Thanks syh");
        }
    }



    static class UpperCaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }
}