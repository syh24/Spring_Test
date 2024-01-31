package spring.test.Proxy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

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

        //다이나믹 프록시 적용
        Hello dynamicProxy = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget())
        );
        Assertions.assertThat(dynamicProxy.sayHello("syh")).isEqualTo("HELLO SYH");
        Assertions.assertThat(dynamicProxy.sayHi("syh")).isEqualTo("HI SYH");
        Assertions.assertThat(dynamicProxy.sayThanks("syh")).isEqualTo("THANKS SYH");
    }
}