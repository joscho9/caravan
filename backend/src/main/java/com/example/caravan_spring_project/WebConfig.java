package com.example.caravan_spring_project;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost",
                        "http://localhost:5173", // Für lokale Entwicklung
                        "http://localhost:3000",      
                        "http://frontend:3000",       // Für Container-Kommunikation
                        "http://my-react-container:3000",  // Alternativ Container-Name
                        "http://wohnwagenvermietung-niederkassel.de/"
                        
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }


}
