package com.alexandreconrado.easylife.scripts.mailsending;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "SendMailTask";
    private final String fromEmail;
    private final String fromPassword;
    private final String toEmail;
    private final String emailSubject;
    private final String emailBody;

    public SendMailTask(String fromEmail, String fromPassword, String toEmail, String emailSubject, String emailBody) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        this.toEmail = toEmail;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", "smtp.mail.yahoo.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        Log.i("SendMailTask", "passou1");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                Log.i("SendMailTask", "passou2");
                return new PasswordAuthentication(fromEmail, fromPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(emailSubject);
            message.setText(emailBody, "UTF-8", "html");

            Transport.send(message);
            Log.i("SendMailTask", "passou3");
            Log.i(TAG, "Email sent successfully.");
        } catch (MessagingException e) {
            Log.e(TAG, "Error sending email.", e);
        }

        return null;
    }
}
