package dasturlash.uz.config;

import dasturlash.uz.entity.ProfileEntity;
import dasturlash.uz.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username -> 998915721213
        Optional<ProfileEntity> optional = profileRepository.findByPhoneAndVisibleTrue(username);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        ProfileEntity profile = optional.get();

        CustomUserDetails userDetails = new CustomUserDetails(profile);
        return userDetails;
    }
}
