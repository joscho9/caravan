package com.example.caravan_spring_project.service;

import com.example.caravan_spring_project.dto.BookingDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class TemplateService {

    public String renderBookingConfirmed(BookingDTO booking) {
        Map<String, String> variables = new HashMap<>();
        variables.put("caravanName", booking.getCaravanName());
        variables.put("startDate", booking.getStartDate().toString());
        variables.put("endDate", booking.getEndDate().toString());
        variables.put("customerName", booking.getName());
        variables.put("customerEmail", booking.getEmail());
        
        return renderTemplate("templates/booking-confirmed.html", variables);
    }

    public String renderBookingRejected(BookingDTO booking) {
        Map<String, String> variables = new HashMap<>();
        variables.put("caravanName", booking.getCaravanName());
        variables.put("startDate", booking.getStartDate().toString());
        variables.put("endDate", booking.getEndDate().toString());
        variables.put("customerName", booking.getName());
        variables.put("customerEmail", booking.getEmail());
        
        return renderTemplate("templates/booking-rejected.html", variables);
    }

    public String renderBookingError(String errorMessage) {
        Map<String, String> variables = new HashMap<>();
        variables.put("errorMessage", errorMessage);
        
        return renderTemplate("templates/booking-error.html", variables);
    }

    private String renderTemplate(String templatePath, Map<String, String> variables) {
        try {
            ClassPathResource resource = new ClassPathResource(templatePath);
            if (!resource.exists()) {
                throw new IOException("Template not found: " + templatePath);
            }
            String template = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            
            // Simple template variable replacement
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
            
            return template;
        } catch (IOException e) {
            // Fallback to inline HTML if template loading fails
            System.err.println("Template loading failed: " + e.getMessage());
            return createFallbackHtml(templatePath, variables, e.getMessage());
        }
    }
    
    private String createFallbackHtml(String templatePath, Map<String, String> variables, String error) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Booking Status</title></head>");
        html.append("<body style='font-family: Arial, sans-serif; text-align: center; padding: 50px;'>");
        
        if (templatePath.contains("confirmed")) {
            html.append("<h2 style='color: green;'>Buchung erfolgreich bestaetigt!</h2>");
            html.append("<p>Die Termine wurden automatisch fuer den Wohnwagen gesperrt.</p>");
        } else if (templatePath.contains("rejected")) {
            html.append("<h2 style='color: orange;'>Buchung abgelehnt</h2>");
            html.append("<p>Die Buchung wurde erfolgreich abgelehnt.</p>");
        } else {
            html.append("<h2 style='color: red;'>Fehler</h2>");
            html.append("<p>").append(variables.getOrDefault("errorMessage", "Ein Fehler ist aufgetreten")).append("</p>");
        }
        
        // Add booking details if available
        if (variables.containsKey("caravanName")) {
            html.append("<div style='margin: 20px 0; padding: 20px; background: #f8f9fa; border-radius: 5px;'>");
            html.append("<p><strong>Wohnwagen:</strong> ").append(variables.get("caravanName")).append("</p>");
            html.append("<p><strong>Zeitraum:</strong> ").append(variables.get("startDate")).append(" bis ").append(variables.get("endDate")).append("</p>");
            html.append("<p><strong>Kunde:</strong> ").append(variables.get("customerName")).append("</p>");
            html.append("</div>");
        }
        
        html.append("<p style='color: #666; font-size: 12px;'>Template Error: ").append(error).append("</p>");
        html.append("</body></html>");
        
        return html.toString();
    }
}