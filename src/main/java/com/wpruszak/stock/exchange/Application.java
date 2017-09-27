package com.wpruszak.stock.exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableJpaRepositories
public class Application {

    @Bean
    public Jaxb2Marshaller getMarshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setPackagesToScan("com.wpruszak.stock.exchange.entity");
        Map<String,Object> map = new HashMap<>();
        map.put("jaxb.formatted.output", true);
        jaxb2Marshaller.setMarshallerProperties(map);
        return jaxb2Marshaller;
    }

    public static void main(String[] args) throws IOException {

        if (args.length > 0 && args[0].equals("download")) {
            SpringApplication.run(Application.class, args).close();
        } else {
            SpringApplication.run(Application.class, args);
        }
    }
}
