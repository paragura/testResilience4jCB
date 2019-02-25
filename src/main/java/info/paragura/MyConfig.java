package info.paragura;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static info.paragura.MyCircuitBreakerAspect.MY_CIRCUIT_BREAKER_NAME_PREFIX;

@Configuration
public class MyConfig {

    @Bean(name = MY_CIRCUIT_BREAKER_NAME_PREFIX + "aClient1")
    public MyClient a1() {
        return new MyClient();
    }

    @Bean(name = MY_CIRCUIT_BREAKER_NAME_PREFIX + "aClient2")
    public MyClient a2() {
        return new MyClient();
    }
}
