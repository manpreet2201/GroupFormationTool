package CSCI5308.GroupFormationTool.resetpassword;

import CSCI5308.GroupFormationTool.AccessControl.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class DefaultEmailService implements IEmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Async
	public void sendEmail(User user, HttpServletRequest request) {


		String appUrl = request.getScheme() + "://" + request.getServerName();

		// Email message
		SimpleMailMessage resetPasswordEmail = new SimpleMailMessage();
		resetPasswordEmail.setFrom("support@group21.com");
		resetPasswordEmail.setTo(user.getEmail());
		resetPasswordEmail.setSubject("Password Reset Request");

		resetPasswordEmail.setText("To reset your password, click the link below:\n" + appUrl + "/reset_token/" + user.getResetToken());

		mailSender.send(resetPasswordEmail);
	}

}