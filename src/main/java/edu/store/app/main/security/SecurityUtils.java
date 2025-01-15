package edu.store.app.main.security;

import com.vaadin.flow.server.HandlerHelper.RequestType;
import com.vaadin.flow.shared.ApplicationConstants;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {

    public static Set<String> getAuthorities() {
        UserDetails userDetails = getUserDetails();
        if (userDetails != null) {
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            return authorities.stream().map(e -> e.getAuthority()).collect(Collectors.toSet());
        }

        return Collections.emptySet();
    }

    public static boolean isGranted(String role) {
        return getAuthorities().contains(role);
    }

    static boolean isFrameworkInternalRequest(HttpServletRequest request) {
        final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return (
            parameterValue != null &&
            Stream.of(RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue))
        );
    }

    public static boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public static UserDetails getUserDetails() {
        SecurityContext context = SecurityContextHolder.getContext();

        if (context.getAuthentication() == null) {
            return null;
        }

        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) context.getAuthentication().getPrincipal();
        }

        return null;
    }
}
