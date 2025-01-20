package edu.store.utils;

import edu.store.app.main.security.SecurityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ValidationUtils {

    static SecurityService securityService;

    public ValidationUtils(SecurityService securityService) {
        this.securityService = securityService;
    }

    public static Boolean passwordIsValid(String pwd) {
        if (StringUtils.isEmpty(pwd) || pwd.length() < 6) {
            return false;
        }

        return pwd.matches("(?=(?:.*[a-zA-Z]){1,})(?=(?:.*[0-9]){1,})^[a-zA-Z0-9@-]*$");
    }

    public static boolean hasRole(String role) {
        System.out.println("Roles for user: " +
                securityService.getAuthenticatedUser().getAuthorities());
        return (
            StringUtils.isNotBlank(role) &&
            securityService
                .getAuthenticatedUser()
                .getAuthorities()
                .stream()
                .anyMatch(f -> f.getAuthority().toLowerCase().contains(role.toLowerCase()))
        );
    }
}
