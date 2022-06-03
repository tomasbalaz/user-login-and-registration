package sk.balaz.userloginandregistraton.email;

public interface EmailSender {

    void send(String to, String email);
}
