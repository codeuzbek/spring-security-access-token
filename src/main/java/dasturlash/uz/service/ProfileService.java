package dasturlash.uz.service;

import dasturlash.uz.config.CustomUserDetails;
import dasturlash.uz.dto.JwtDTO;
import dasturlash.uz.dto.ProfileDTO;
import dasturlash.uz.dto.TokenDTO;
import dasturlash.uz.dto.aut.AuthDTO;
import dasturlash.uz.entity.ProfileEntity;
import dasturlash.uz.exp.AppBadRequestException;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    public ProfileDTO registration(ProfileDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByPhoneAndVisibleTrue(dto.getPhone());
        if (optional.isPresent()) {
            return null;
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPhone(dto.getPhone());

        entity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));

        entity.setRole(dto.getRole());

        profileRepository.save(entity);

        dto.setId(entity.getId());
        return dto;
    }


    public ProfileDTO authorization(AuthDTO authDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getPhone(), authDTO.getPassword()));

            if (authentication.isAuthenticated()) {
                CustomUserDetails profile = (CustomUserDetails) authentication.getPrincipal();

                ProfileDTO response = new ProfileDTO();
                response.setName(profile.getName());
                response.setSurname(profile.getSurname());
                response.setPhone(profile.getUsername());
                response.setRole(profile.getRole());
                response.setAccessToken(JwtUtil.encode(profile.getUsername(), profile.getRole().name()));
                response.setRefreshToken(JwtUtil.refreshToken(profile.getUsername(), profile.getRole().name()));
                return response;
            }
            throw new AppBadRequestException("Phone or password wrong");
        } catch (BadCredentialsException e) {
            throw new AppBadRequestException("Phone or password wrong");
        }
    }

    public TokenDTO getNewAccessToken(TokenDTO dto) {
        try {
            if (JwtUtil.isValid(dto.getRefreshToken())) {
                JwtDTO jwtDTO = JwtUtil.decode(dto.getRefreshToken());

                Optional<ProfileEntity> optional = profileRepository.findByPhoneAndVisibleTrue(jwtDTO.getPhone());

                if (optional.isPresent()) {
                    ProfileEntity profile = optional.get();

                    TokenDTO response = new TokenDTO();
                    response.setAccessToken(JwtUtil.encode(profile.getPhone(), profile.getRole().name()));
                    response.setRefreshToken(JwtUtil.refreshToken(profile.getPhone(), profile.getRole().name()));
                    return response;
                }
            }
        } catch (JwtException e) {

        }
        throw new AppBadRequestException("Invalid token");
    }
}
