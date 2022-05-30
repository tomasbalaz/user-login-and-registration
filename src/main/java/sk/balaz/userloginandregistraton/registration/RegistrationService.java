package sk.balaz.userloginandregistraton.registration;

import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    public String register(RegistrationRequest registrationRequest) {
        return "works";
    }
}
