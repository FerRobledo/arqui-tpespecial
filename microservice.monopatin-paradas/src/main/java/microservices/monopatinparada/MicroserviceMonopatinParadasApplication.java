package microservices.monopatinparada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceMonopatinParadasApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceMonopatinParadasApplication.class, args);
    }

}
