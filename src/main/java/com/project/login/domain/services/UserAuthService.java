package com.project.login.domain.services;

import com.project.login.domain.entitys.Login;
import com.project.login.domain.entitys.enums.UserRole;
import com.project.login.domain.entitys.user.User;
import com.project.login.domain.exceptions.UserAuthServiceException;
import com.project.login.domain.repositorys.UserRepository;
import com.project.login.infrastructure.security.TokenService;
import com.project.login.outside.representation.model.input.ForgotPasswordInput;
import com.project.login.outside.representation.model.input.LoginInput;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
        //TODO: Concertar bug que manda o email para o usuário mesmo dando conflito no banco de dados
        try {
            saveToTheDatabase(user);
            this.sendConfirmationEmail(user);
        } catch (DataIntegrityViolationException exception) {
            throw new DataIntegrityViolationException(exception.getMessage());
        }
    }

    /**
     * Responsible for authenticating every user that wants to login
     * and do requests to my application
     *
     * @param data of a user trying to login
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

        return new Login(user.getName(), user.getUsername(), user.getUserRole(), token);
    }

    /**
     * changes the state of the user(locked, enabled) confirming the email,
     * now the user can access the application
     *
     * @param username identifier
     * @return Login information, front-end can instantly login after email confirmation
     */
    @Transactional
    public Login confirmEmail(String username) {
        try{
            User user = changeEnabledAndLockedState(username);
            String token = tokenService.generateToken(user);
            return new Login(user.getName(), user.getUsername(), user.getUserRole(), token);
        } catch (DataIntegrityViolationException exception){
            throw new DataIntegrityViolationException("User Already Exists");
        }
    }

    /**
     * Password change method updated the user's new password
     *
     * @param username      to identify
     * @param newPassword change the old to this new password
     */
    @Transactional
    public void changePassword(String username, String newPassword) {
        User user = userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User Doesn't exist"));

        String encryptedPassword = new BCryptPasswordEncoder().encode(newPassword);
        user.setPassword(encryptedPassword);

        userRepository.save(user);
    }

    public void forgotPassword(ForgotPasswordInput forgotPassword){
        User user = userRepository
            .findByEmail(forgotPassword.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User Doesn't Exist"));

        sendForgotPasswordEmail(user);
    }

    /**
     * Sends a change password email using EmailSenderService
     *
     * @param user identifier
     */
    private void sendForgotPasswordEmail(User user) {
        String link = "http://localhost:4200/change-password/" + user.getUsername();
        String body =
            "<html>\n" +
                "<body>\n" +
                "    <p>Click the button below to change your password</p>\n" +
                "    <a href=\"" + link + "\" style=\"background-color: #4CAF50; border: none; color: white; padding: 15px 32px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; margin: 4px 2px; cursor: pointer;\">Change Password</a>\n" +
                "</body>\n" +
                "</html>";

        emailSenderService.sendEmail(user.getEmail(), "Change Your Password", body);
    }

    /**
     * Sends a confirmation email using EmailSenderService
     *
     * @param user identifier
     */
    private void sendConfirmationEmail(User user) {
        String link = "http://localhost:4200/register/confirm/" + user.getUsername();
        String body =
            "<html>\n" +
                "<body>\n" +
                "    <p>Click the button below to confirm your registration:</p>\n" +
                "    <a href=\"" + link + "\" style=\"background-color: #4CAF50; border: none; color: white; padding: 15px 32px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; margin: 4px 2px; cursor: pointer;\">Confirm Registration</a>\n" +
                "</body>\n" +
                "</html>";

        emailSenderService.sendEmail(user.getEmail(), "Confirm your email", body);
    }

    private void saveToTheDatabase(User user) throws UserAuthServiceException {
        try{
            String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());

            if (user.getUsername().contains("ADMIN")) {
                user.setUserRole(UserRole.ADMIN);
            } else {
                user.setUserRole(UserRole.USER);
            }

            user.setLocked(false);
            user.setEnabled(false);
            user.setPassword(encryptedPassword);

            userRepository.saveAndFlush(user);
        }catch (DataIntegrityViolationException exception){
            throw new DataIntegrityViolationException("User Already Exists");
        }
    }

    private User changeEnabledAndLockedState(String username){
        try{
            User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Doesn't exist"));

            user.setEnabled(true);
            user.setLocked(true);

            return userRepository.saveAndFlush(user);
        }catch (DataIntegrityViolationException exception){
            throw new DataIntegrityViolationException("User Already in Use");
        }
    }
}
