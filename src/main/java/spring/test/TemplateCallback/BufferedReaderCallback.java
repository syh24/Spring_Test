package spring.test.TemplateCallback;

import java.io.BufferedReader;
import java.io.IOException;

public interface BufferedReaderCallback {

    public Integer doSomethingWithReader(BufferedReader br) throws IOException;
}
