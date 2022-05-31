package sk.balaz.userloginandregistraton.registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sk.balaz.userloginandregistraton.appuser.AppUser;
import sk.balaz.userloginandregistraton.appuser.AppUserRole;
import sk.balaz.userloginandregistraton.appuser.AppUserService;
import sk.balaz.userloginandregistraton.registration.token.ConfirmationToken;
import sk.balaz.userloginandregistraton.registration.token.ConfirmationTokenService;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;

    private final AppUserService appUserService;

    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest registrationRequest) {

        boolean isEmailValid = emailValidator.test(registrationRequest.getEmail());

        if(!isEmailValid) {
            //throw new IllegalStateException("email not valid");
            throw new IllegalStateException(String.format("email %s not valid", registrationRequest.getEmail()));
        }

        return appUserService.signUp(
                new AppUser(
                        registrationRequest.getFirstName(),
                        registrationRequest.getLastName(),
                        registrationRequest.getEmail(),
                        registrationRequest.getPassword(),
                        AppUserRole.USER
                )
        );
    }

    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if(confirmationToken.getConfirmedAt() != null) {
            throw  new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();
        if(expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }
}
