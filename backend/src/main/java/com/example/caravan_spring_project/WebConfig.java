package com.example.caravan_spring_project;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Development
        // registry.addMapping("/**")
        //         .allowedOriginPatterns("*")
        //         .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        //         .allowedHeaders("*")
        //         .allowCredentials(true);
        
        // Production
        registry.addMapping("/**").allowedOrigins(
            "http://localhost",
            "http://localhost:5173",          
            "http://localhost:3000",
            "http://frontend:3000",        
            "http://my-react-container:3000", 
            "https://wohnwagenvermietung-niederkassel.de",
            "https://www.wohnwagenvermietung-niederkassel.de",
            "https://wohnwagenvermietung-frankfurt.de",
            "https://www.wohnwagenvermietung-frankfurt.de",
            "https://wohnwagenvermietung-koeln.de",
            "https://www.wohnwagenvermietung-koeln.de",
            "https://wohnwagenvermietung-aschaffenburg.de",
            "https://www.wohnwagenvermietung-aschaffenburg.de",
            "https://wohnwagenvermietung-hainburg.de"
            )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/app/uploads/");
    }


}
