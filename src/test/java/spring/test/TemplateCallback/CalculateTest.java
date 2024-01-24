package spring.test.TemplateCallback;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

class CalculateTest {
    Calculate calculate;

    @BeforeEach
    void setup() {
        this.calculate = new Calculate();
    }

    @Test
    void sumOfNumbers() throws IOException {
        Integer sum = calculate.calcSum("src/main/resources/numbers.txt");
        assertThat(sum).isEqualTo(10);
    }

    @Test
    void multiplyOfNumbers() throws IOException {
        Integer multiply = calculate.calcMultiply("src/main/resources/numbers.txt");
        assertThat(multiply).isEqualTo(24);
    }

    @Test
    void concatenateStrings() throws IOException {
        String result = calculate.concatenate("src/main/resources/numbers.txt");
        assertThat(result).isEqualTo("1234");
    }
}