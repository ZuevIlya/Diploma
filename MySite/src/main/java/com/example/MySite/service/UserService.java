package com.example.MySite.service;

import com.example.MySite.models.Events;
import com.example.MySite.models.Role;
import com.example.MySite.models.User;
import com.example.MySite.repositories.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SmtpMailSender smtpMailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public boolean addUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false; // Не добавляем пользователя, если такой уже имеется
        }

        // user.setActive(true);
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Пароль зашифрован при регистрации
        userRepository.save(user);

        if (StringUtils.isNotBlank(user.getEmail())) {
            String message = String.format(
                    "Здравствуй, %s! \n" + "Добро пожаловать на AllTournaments! Пожалуйста, перейди по ссылке для завершения регистрации: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            smtpMailSender.send(user.getEmail(), "Activation code", message);
        }
        return true;
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActive(true);
        user.setActivationCode(null);
        userRepository.save(user);
        return true;
    }

    public void updateProfile(User user, String password, String email, String name, String secondName, String country, int age) {

        // Достаём старые данные
        String userEmail = user.getEmail();
        String userName = user.getName();
        String userSecondName = user.getSecondName();
        String userCountry = user.getCountry();
        int userAge = user.getAge();

        // Проверка на изменение данных
        // boolean isEmailChanged = (email != null && !email.equals(userEmail)) || (userEmail != null && !userEmail.equals(email));
        boolean isEmailChanged = (email != null && !email.equals(userEmail));
        if (isEmailChanged) {
            user.setEmail(email);
        }

        boolean isNameChanged = (name != null && !name.equals(userName));
        if (isNameChanged) {
            user.setName(name);
        }

        boolean isSecondNameChanged = (secondName != null && !secondName.equals(userSecondName));
        if (isSecondNameChanged) {
            user.setSecondName(secondName);
        }

        boolean isCountryChanged = (country != null && !country.equals(userCountry));
        if (isCountryChanged) {
            user.setCountry(country);
        }

        boolean isAgeChanged = (age != 0 && age != userAge);
        if (isAgeChanged) {
            user.setAge(age);
        }

        if ((StringUtils.isNotBlank(email)) && isEmailChanged) {
            user.setActivationCode(UUID.randomUUID().toString());
        }

        if (StringUtils.isNotBlank(password)) {
            user.setPassword(passwordEncoder.encode(password)); // Для шифрования нового пароля
        }

        userRepository.save(user);
        if (isEmailChanged) {
            sendMessage(user);
        }
    }

    private void sendMessage(User user) {
        if (StringUtils.isNotBlank(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" + "Welcome to mySite. Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            smtpMailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void registration(User user, Events event) {
        user.getTournaments().add(event);
        userRepository.save(user);
    }
}
