package sk.balaz.userloginandregistraton.registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sk.balaz.userloginandregistraton.appuser.AppUser;
import sk.balaz.userloginandregistraton.appuser.AppUserRole;
import sk.balaz.userloginandregistraton.appuser.AppUserService;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;

    private final AppUserService appUserService;

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
}
