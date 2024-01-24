package spring.test.TemplateCallback;

import java.io.BufferedReader;
import java.io.IOException;

public interface LineCallback<T> {

    T doSomethingWithLine(String line, T ret) throws IOException;
}
