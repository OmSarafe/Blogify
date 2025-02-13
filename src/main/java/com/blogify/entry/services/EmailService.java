package com.blogify.entry.services;

import com.blogify.entry.enteties.User;
import com.blogify.entry.repositories.UserRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    UserRepositories userRepositories;
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("spring.mail.username")
    private String email;
    @Async
    public void sendEmail(String to,String subject,String body){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(email);
        javaMailSender.send(message);
    }

    @Scheduled(cron = " 30 45 20 * * ?")
    @Async
    public void sendPromotionalEmails(){
        List<User>users =userRepositories.findAll();
        users.forEach(user -> sendEmail(user.getEmail(),"Topic",getBody(user)));
    }

    public String getBody(User user){
        return "Dear "+user.getUsername();
    }
}
