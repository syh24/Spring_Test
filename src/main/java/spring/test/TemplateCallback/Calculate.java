package spring.test.TemplateCallback;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculate {

    //클라이언트 콜백 메소드 생성 및 템플릿 메소드 호출
    public Integer calcSum(String filepath) throws IOException {
        LineCallback<Integer> sumCallback = new LineCallback<Integer>() {
            //콜백 메소드
            @Override
            public Integer doSomethingWithLine(String line, Integer ret) throws IOException {
                return ret + Integer.valueOf(line);
            }
        };
        return lineReadTemplate(filepath, 0, sumCallback);
    }

    //클라이언트 콜백 메소드 생성 및 템플릿 메소드 호출
    public Integer calcMultiply(String filepath) throws IOException {
        LineCallback<Integer> multiplyCallback = new LineCallback<Integer>() {
            //콜백 메소드
            @Override
            public Integer doSomethingWithLine(String line, Integer ret) throws IOException {
                return ret * Integer.valueOf(line);
            }
        };
        return lineReadTemplate(filepath, 1, multiplyCallback);
    }

    public String concatenate(String filepath) throws IOException {
        LineCallback<String> concatenateCallback = new LineCallback<>() {

            @Override
            public String doSomethingWithLine(String line, String ret) throws IOException {
                return ret + line;
            }
        };

        return lineReadTemplate(filepath, "", concatenateCallback);
    }

    //템플릿 메소드
    public <T> T lineReadTemplate(String filepath, T intVal, LineCallback<T> callback) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            T ret = intVal;
            String line = null;
            while ((line = br.readLine()) != null) {
                ret = callback.doSomethingWithLine(line, ret);
            }
            return ret;
        } catch(IOException e) {
            System.out.println("error = " + e);
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println("error = " + e);
                }
            }
        }
    }
}
