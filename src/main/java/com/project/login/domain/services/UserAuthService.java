package com.project.login.domain.services;

import com.project.login.domain.entitys.Login;
import com.project.login.domain.entitys.enums.UserRole;
import com.project.login.domain.entitys.user.User;
import com.project.login.domain.exceptions.UserAuthServiceException;
import com.project.login.domain.repositorys.UserRepository;
import com.project.login.infrastructure.security.TokenService;
import com.project.login.outside.representation.model.input.LoginInput;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final EmailSenderService emailSenderService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Responsible for saving all users inside the database.
     * method defines the role, the state(locked, enabled) and
     * encrypts the password, also calls the confirmation email method
     * that sends an email to the user
     *
     * @param user registered
     */
    @Transactional
    public void register(User user) {
        if (user == null) {
            throw new UserAuthServiceException("User is Empty");
        }

        //Sends the confirmation Email
        this.confirmationEmail(user);

        //Encrypting Password
        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        if (user.getUsername().contains("ADMIN")) {
            user.setUserRole(UserRole.ADMIN);
        } else {
            user.setUserRole(UserRole.USER);
        }

        user.setLocked(false);
        user.setEnabled(false);
        user.setPassword(encryptedPassword);

        userRepository.save(user);
    }

    /**
     * Responsible for authenticating every user that wants to login
     * and do requests to my application
     *
     * @param data of a logged user
     * @return Login information(token, role, name)
     */
    @Transactional
    public Login login(LoginInput data) {
        String token;
        User user = userRepository
            .findByUsernameOrEmail(data.login(), data.login())
            .orElseThrow(() -> new UsernameNotFoundException("User Doesn't exist"));

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        if (auth.isAuthenticated()) {
            token = tokenService.generateToken(user);
        } else {
            throw new RuntimeException("Is not authenticated");
        }

        return new Login(user.getName(), user.getUserRole(), token);
    }

    //    @Transactional
    //    public Login loginOAuth(LoginInput data){
    //        return new Login(null,null,null);
    //  }

    /**
     * changes the state of the user(locked, enabled) confirming the email,
     * now the user can access the application
     *
     * @param username identifier
     * @param token    to access the application
     * @return Login information, front-end can instantly login after email confirmation
     */
    @Transactional
    public Login confirmEmail(String username, String token) {
        User user = userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User Doesn't exist"));

        user.setEnabled(true);
        user.setLocked(true);

        userRepository.save(user);

        return new Login(user.getName(), user.getUserRole(), token);
    }

    /**
     * Sends a confirmation email using EmailSenderService
     *
     * @param user identifier
     */
    private void confirmationEmail(User user) {
        //Generating Token
        String token = tokenService.generateToken(user);

        //Sending Confirmation Email
        String body = "http://localhost:8080/api/v1/auth/register/confirm?username="
            + user.getUsername()
            + "&token="
            + token;

        emailSenderService.sendEmail(user.getEmail(), "Confirm your email", body);
    }

    /**
     * Password change method updated the user's new password
     *
     * @param email       to identify
     * @param newPassword change the old to this new password
     */
    @Transactional
    public void changePassword(String email, String newPassword) {
        User user = userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User Doesn't exist"));

        String encryptedPassword = new BCryptPasswordEncoder().encode(newPassword);
        user.setPassword(encryptedPassword);

        userRepository.save(user);
    }
}
