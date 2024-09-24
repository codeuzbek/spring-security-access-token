package dasturlash.uz.util;

import dasturlash.uz.config.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SpringSecurityUtil {

    public static String getCurrentProfileId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return user.getId();
    }

    public static Collection<GrantedAuthority> getProfileGrantedAuthorityList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        Collection<GrantedAuthority> roles = (Collection<GrantedAuthority>) user.getAuthorities();
        return roles;
    }

    public static List<String> getProfileRoleList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        Collection<? extends GrantedAuthority> roles = user.getAuthorities();
        List<String> rolesList = new ArrayList<String>();
        for (GrantedAuthority role : roles) {
            rolesList.add(role.getAuthority());
        }

        return rolesList;
    }

}
