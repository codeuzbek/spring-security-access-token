package dasturlash.uz.controller;

import dasturlash.uz.dto.ProfileDTO;
import dasturlash.uz.dto.TokenDTO;
import dasturlash.uz.dto.aut.AuthDTO;
import dasturlash.uz.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("/registration")
    public ResponseEntity<ProfileDTO> create(@RequestBody ProfileDTO dto) {
        ProfileDTO result = profileService.registration(dto);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/authorization")
    public ResponseEntity<ProfileDTO> authorization(@RequestBody AuthDTO dto) {
        ProfileDTO result = profileService.authorization(dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/access-token")
    public ResponseEntity<TokenDTO> getAccessToken(@RequestBody TokenDTO dto) {
        TokenDTO newAccessToken = profileService.getNewAccessToken(dto);
        return ResponseEntity.ok(newAccessToken);
    }
}
