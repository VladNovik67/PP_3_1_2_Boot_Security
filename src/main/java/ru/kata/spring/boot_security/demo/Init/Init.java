package ru.kata.spring.boot_security.demo.Init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImp;
import ru.kata.spring.boot_security.demo.service.UsersServiceImp;

import java.util.HashSet;
import java.util.Set;

@Component
public class Init implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleServiceImp roleServiceImp;
    private final UsersServiceImp usersServiceImp;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public Init(RoleServiceImp roleServiceImp, UsersServiceImp usersServiceImp, PasswordEncoder passwordEncoder) {
        this.roleServiceImp = roleServiceImp;
        this.usersServiceImp = usersServiceImp;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String str = "user";
        Role userRole = new Role(1L, "USER");
        if (roleServiceImp.getByName("ROLE_USER") == null) {
            roleServiceImp.save(userRole);
        }
        Role adminRole = new Role(2L, "ADMIN");
        if (roleServiceImp.getByName("ROLE_ADMIN") == null) {
            roleServiceImp.save(adminRole);
        }
        if (usersServiceImp.findByUsername("user") == null) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setEmail("test_email");
            Set<Role> role = new HashSet<>();
            role.add(userRole);
            user.setRoles(role);
            usersServiceImp.saveUser(user);
        }
        if (usersServiceImp.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("test_email");
            Set<Role> role = new HashSet<>();
            role.add(adminRole);
            admin.setRoles(role);
            usersServiceImp.saveUser(admin);
        }
    }
}
