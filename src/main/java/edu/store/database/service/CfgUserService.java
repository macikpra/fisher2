/* (C)2022 www.neumann-itcs.com */
package edu.store.database.service;

import edu.store.database.entities.CfgRole;
import edu.store.database.entities.CfgUser;
import edu.store.database.entities.CfgUserRoleMap;
import edu.store.database.repositories.CfgRoleRepository;
import edu.store.database.repositories.CfgUserRepository;
import edu.store.database.repositories.CfgUserRoleMapRepository;
import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CfgUserService implements UserDetailsService {

    private final CfgUserRepository repo;
    private final CfgUserRoleMapRepository userRoleMapRepository;
    private final CfgRoleRepository cfgRoleRepository;

    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(CfgUserService.class);

    public CfgUserService(
        CfgUserRepository repo,
        CfgUserRoleMapRepository userRoleMapRepository,
        CfgRoleRepository cfgRoleRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.repo = repo;
        this.userRoleMapRepository = userRoleMapRepository;
        this.cfgRoleRepository = cfgRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CfgUser> findAll() {
        return repo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername: " + username);
        logger.info("calling UserDetails loadUserByUsername(String username) with username: {}", username);
        CfgUser user = findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        CfgUserRoleMap map = userRoleMapRepository.findOneByCfgUserId(user.getId());
        CfgRole roles = cfgRoleRepository.findOneById(map.getCfgRoleId());
        logger.info(
            "User found with user: {}, email: {}, passwd: {}, and roles: {}",
            user.getName(),
            user.getEmail(),
            user.getPswd(),
            roles
        );
        List<GrantedAuthority> auth;
        if (roles == null) auth = AuthorityUtils.NO_AUTHORITIES;
        else auth = AuthorityUtils.commaSeparatedStringToAuthorityList(roles.getName());

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPswd(), auth);
    }

    public CfgUser findByEmail(String email) {
        return repo.findByEmail(email);
    }

    public Pair<CfgUser, CfgUserRoleMap> createNewUser(
        String firstName,
        String lastName,
        String password,
        String pesel,
        String phoneNumber,
        String roleName
    ) {
        CfgUser user = new CfgUser();
        user.setEnabled(true);
        user.setName(firstName + "_" + lastName);
        user.setEmail(firstName + "_" + lastName);
        user.setDescription(String.format("pesel: %s, numerTelefonu: %s", pesel, phoneNumber));
        user.setPswd(passwordEncoder.encode(password));
        user = repo.save(user);
        logger.debug("createNewUser: " + user);

        CfgUserRoleMap userRoleMap = new CfgUserRoleMap();
        userRoleMap.setCfgUserId(user.getId());
        userRoleMap.setCfgRoleId(cfgRoleRepository.findOneByName(roleName).getId());
        userRoleMap = userRoleMapRepository.save(userRoleMap);
        logger.debug("userRoleMap created: " + userRoleMap);

        return new ImmutablePair<CfgUser, CfgUserRoleMap>(user, userRoleMap);
    }
}
