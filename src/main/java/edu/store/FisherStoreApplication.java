package edu.store;

import edu.store.database.entities.CfgRole;
import edu.store.database.entities.Pracownik;
import edu.store.database.entities.TypSprzetu;
import edu.store.database.entities.TypTowaru;
import edu.store.database.repositories.CfgRoleRepository;
import edu.store.database.repositories.PracownikRepository;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.store.database.repositories.TypSprzetuRepository;
import edu.store.database.repositories.TypTowaruRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@EnableJpaRepositories("edu.store.*")
@EntityScan(basePackages = "edu.store.*")
@ComponentScan("edu.store.*")
@SpringBootApplication(
    scanBasePackages = { "edu.store.*.*" },
    exclude = { ErrorMvcAutoConfiguration.class, SecurityFilterAutoConfiguration.class }
)
@EnableTransactionManagement
@Log // TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class FisherStoreApplication {

    @Autowired
    PracownikRepository pracownikRepository;
    @Autowired
    CfgRoleRepository cfgRoleRepository;
    @Autowired
    TypSprzetuRepository typSprzetuRepository;
    @Autowired
    TypTowaruRepository typTowaruRepository;

    public static void main(String[] args) {
        SpringApplication.run(FisherStoreApplication.class, args);
    }

    @Bean
    public LocaleResolver localeResolver() {
        log.info("hallo in localeResolver...");
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(new Locale("pl"));
        slr.setLocaleAttributeName("session.current.locale");
        slr.setTimeZoneAttributeName("session.current.timezone");
        return slr;
    }

    @Bean
    public CommandLineRunner startup() {
        return args -> {
            List<CfgRole> roles = cfgRoleRepository.findAll();
            if (roles.isEmpty()) {
                createDefaultRoles();
                listAllRoles();
            }
            List<TypSprzetu> sprzety = typSprzetuRepository.findAll();
            if (sprzety.isEmpty()) {
                createSprzetTypes();
                listAllSprzetTypes();
            }
            List<TypTowaru> towaru = typTowaruRepository.findAll();
            if (towaru.isEmpty()) {
                createTowarTypes();
                listAllTowarTypes();
            }
            List<Pracownik> list = pracownikRepository.findAll();
            System.out.println("Lista pracownikow: " + list);

            System.out.println("Database initialized!");
        };
    }

    private void listAllRoles() {
        List<CfgRole> roles = cfgRoleRepository.findAll();
        System.out.println("Roles created: ");
        roles.forEach(System.out::println);
    }
    private void listAllSprzetTypes() {
        List<TypSprzetu> list = typSprzetuRepository.findAll();
        System.out.println("Lista sprzetu: " + list);
        list.forEach(System.out::println);
    }
    private void listAllTowarTypes() {
        List<TypTowaru> list = typTowaruRepository.findAll();
        System.out.println("Lista towaru: " + list);
        list.forEach(System.out::println);
    }
    private void createDefaultRoles() {
        CfgRole roleKierownik = new CfgRole();
        roleKierownik.setCreated(new Date());
        roleKierownik.setEnabled(true);
        roleKierownik.setInsertedAt(new Date());
        roleKierownik.setTrxId(0L);
        roleKierownik.setName("Kierownik");
        roleKierownik.setDescription("Kierownik - Opis");
        roleKierownik.setParentId(BigInteger.valueOf(0L));
        roleKierownik.setInsertedBy("startup application");

        CfgRole roleSprzedawca = new CfgRole();
        roleSprzedawca.setCreated(new Date());
        roleSprzedawca.setEnabled(true);
        roleSprzedawca.setInsertedAt(new Date());
        roleSprzedawca.setTrxId(0L);
        roleSprzedawca.setName("Sprzedawca");
        roleSprzedawca.setDescription("Sprzedawca - Opis");
        roleSprzedawca.setParentId(BigInteger.valueOf(0L));
        roleKierownik.setInsertedBy("startup application");

        CfgRole roleBileter = new CfgRole();
        roleBileter.setCreated(new Date());
        roleBileter.setEnabled(true);
        roleBileter.setInsertedAt(new Date());
        roleBileter.setTrxId(0L);
        roleBileter.setName("Bileter");
        roleBileter.setDescription("Bileter - Opis");
        roleBileter.setParentId(BigInteger.valueOf(0L));
        roleKierownik.setInsertedBy("startup application");

        CfgRole roleSerwisant = new CfgRole();
        roleSerwisant.setCreated(new Date());
        roleSerwisant.setEnabled(true);
        roleSerwisant.setInsertedAt(new Date());
        roleSerwisant.setTrxId(0L);
        roleSerwisant.setName("Serwisant");
        roleSerwisant.setDescription("Serwisant - Opis");
        roleSerwisant.setParentId(BigInteger.valueOf(0L));
        roleKierownik.setInsertedBy("startup application");

        cfgRoleRepository.saveAll(List.of(roleKierownik, roleSprzedawca, roleBileter, roleSerwisant));
    }
    private void createSprzetTypes(){
        TypSprzetu wedka = new TypSprzetu("Wedka", 1L);
        TypSprzetu lodz = new TypSprzetu("Lodz", 2L);
        TypSprzetu ponton = new TypSprzetu("Ponton", 3L);
        TypSprzetu ubrania = new TypSprzetu("Ubrania", 4L);
        TypSprzetu narzedzia = new TypSprzetu("Narzedzia", 5L);
        typSprzetuRepository.saveAll(List.of(wedka, lodz, ponton, ubrania, narzedzia));
    }
    private void createTowarTypes(){
        TypTowaru splawiki = new TypTowaru("Splawiki", 1L);
        TypTowaru przynety = new TypTowaru("Przynety", 2L);
        TypTowaru zanety = new TypTowaru("Zanety", 3L);
        TypTowaru zylki = new TypTowaru("Zylki", 4L);
        typTowaruRepository.saveAll(List.of(splawiki,przynety,zanety,zylki));
    }
}
