package info.paragura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @Autowired
    @Qualifier("MyCircuitBreaker-aClient")
    MyClient client1;


    @Autowired
    @Qualifier("MyCircuitBreaker-aClient2")
    MyClient client2;


    @GetMapping("/a1")
    public String a1() throws Exception {
        return client1.test();
    }


    @GetMapping("/a2")
    public String a2() throws Exception {
        return client2.test();
    }


}
