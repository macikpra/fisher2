package edu.store.rest_api;

import static edu.store.utils.ValidationUtils.passwordIsValid;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import edu.store.database.entities.CfgRole;
import edu.store.database.entities.CfgUser;
import edu.store.database.entities.CfgUserRoleMap;
import edu.store.database.repositories.CfgRoleRepository;
import edu.store.database.repositories.CfgUserRepository;
import edu.store.database.repositories.CfgUserRoleMapRepository;
import edu.store.model.dto.UserRegistrationDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.math.BigInteger;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestApi {

    private final CfgUserRepository cfgUserRepository;

    private final CfgRoleRepository cfgRoleRepository;

    private final CfgUserRoleMapRepository cfgUserRoleMapRepository;
    private final PasswordEncoder passwordEncoder;

    public RestApi(
        CfgUserRepository cfgUserRepository,
        CfgRoleRepository cfgRoleRepository,
        CfgUserRoleMapRepository cfgUserRoleMapRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.cfgUserRepository = cfgUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.cfgRoleRepository = cfgRoleRepository;
        this.cfgUserRoleMapRepository = cfgUserRoleMapRepository;
    }

    @GetMapping("/checkUser")
    @AnonymousAllowed
    @ResponseBody
    public Boolean checkUserExists(@RequestParam String email) {
        return userExists(email);
    }

    @PostMapping("/registerUser")
    @AnonymousAllowed
    @ResponseBody
    @Transactional
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDto newUser, HttpServletRequest request) {
        if (!allFieldsNotNullOrNotEmpty(newUser)) {
            return new ResponseEntity<>("Wszystkie pola muszą byc wypelnione!", HttpStatus.BAD_REQUEST);
        }

        if (userExists(newUser.getEmail())) {
            return new ResponseEntity<>("Użytkownik już istnieje", HttpStatus.BAD_REQUEST);
        }

        if (
            !StringUtils.equals(newUser.getPassword(), newUser.getPassword2()) ||
            !passwordIsValid(newUser.getPassword())
        ) {
            return new ResponseEntity<>(
                "Hasła muszą byc identyczne, muszą zawierac minimum 6 znaków, wymagana przynajmniej jedna litera i jedna cyfra!",
                HttpStatus.BAD_REQUEST
            );
        }

        CfgUser user = new CfgUser();
        user.setEnabled(true);
        user.setName(newUser.getImie() + ", " + newUser.getNazwisko());
        user.setEmail(newUser.getEmail());
        user.setDescription(
            String.format(
                "dataUrodzenia: %s, numerKartyWedkarskiej: %s, numerTelefonu: %s",
                newUser.getDataUrodzenia(),
                newUser.getNumerKartyWedkarskiej(),
                newUser.getNumerTelefonu()
            )
        );
        user.setPswd(passwordEncoder.encode(newUser.getPassword()));

        try {
            CfgUser storedUser = cfgUserRepository.save(user);
            CfgRole roleNormalUser = new CfgRole();
            roleNormalUser.setName("store_user");
            roleNormalUser.setEnabled(true);
            roleNormalUser.setParentId(BigInteger.ZERO);
            roleNormalUser.setDescription("Normalny klient do sklepu online");
            Optional<CfgRole> roleInDb = cfgRoleRepository
                .findAll()
                .stream()
                .filter(r -> "store_user".equals(r.getName()))
                .findFirst();
            if (roleInDb.isEmpty()) {
                roleNormalUser = cfgRoleRepository.save(roleNormalUser);
            } else {
                roleNormalUser = roleInDb.get();
            }

            CfgUserRoleMap userRoleMap = new CfgUserRoleMap();
            userRoleMap.setCfgUserId(storedUser.getId());
            userRoleMap.setCfgRoleId(roleNormalUser.getId());
            userRoleMap = cfgUserRoleMapRepository.save(userRoleMap);

            if (storedUser.getId() == null || roleNormalUser.getId() == null || userRoleMap.getId() == null) {
                return new ResponseEntity<>("Cos poszlo nie tak, wystapil blad", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("Użytkownik zostal poprawnie zapisany!", HttpStatus.OK);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(
                "Cos poszlo nie tak, wystapil blad: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/test-request")
    @AnonymousAllowed
    public ResponseEntity<String> testPostRequest() {
        return ResponseEntity.ok("POST request successful");
    }

    private Boolean userExists(String email) {
        CfgUser requestedUser = cfgUserRepository.findByEmail(email);
        return requestedUser != null && requestedUser.getId() > 0;
    }

    private Boolean allFieldsNotNullOrNotEmpty(UserRegistrationDto user) {
        if (
            StringUtils.isAnyEmpty(
                user.getImie(),
                user.getNazwisko(),
                user.getEmail(),
                user.getNumerKartyWedkarskiej(),
                user.getNumerTelefonu(),
                user.getPassword(),
                user.getPassword2()
            ) ||
            user.getDataUrodzenia() == null
        ) {
            return Boolean.FALSE;
        }

        return true;
    }
}
