package edu.store;

import edu.store.database.entities.CfgRole;
import edu.store.database.entities.Pracownik;
import edu.store.database.repositories.CfgRoleRepository;
import edu.store.database.repositories.PracownikRepository;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
            List<Pracownik> list = pracownikRepository.findAll();
            System.out.println("Lista pracownikow: " + list);

            System.out.println("Database initialized!");
        };
    }

    private void listAllRoles() {
        List<CfgRole> roles = cfgRoleRepository.findAll();
        System.out.println("Roles created: ");
        roles.stream().forEach(System.out::println);
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
}
