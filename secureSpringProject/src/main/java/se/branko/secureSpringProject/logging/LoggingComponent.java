package se.branko.secureSpringProject.logging;

import org.springframework.stereotype.Component;

@Component
public class LoggingComponent {
    public void log(String message) {
        System.out.println("[LOGG] " + message);
    }
}
