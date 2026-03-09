package Grupo11.Seminario.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {
    
    @Bean
    public RestTemplate get_rest_template(){
        return new RestTemplate();
    }
}
