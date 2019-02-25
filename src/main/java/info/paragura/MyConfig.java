package info.paragura;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {

    @Bean(name = "MyCircuitBreaker-aClient")
    public MyClient a1() {
        return new MyClient();
    }

    @Bean(name = "MyCircuitBreaker-aClient2")
    public MyClient a2() {
        return new MyClient();
    }
}
