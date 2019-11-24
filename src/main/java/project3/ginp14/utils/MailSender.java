package project3.ginp14.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

public class MailSender {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("vohophuongnam@gmail.com");

        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World \n Spring Boot Email");
        msg.setFrom("NhaHang");
        System.out.println("Before Send");
        System.out.println(msg);
        javaMailSender.send(msg);
        System.out.println("After Send");
    }
}
