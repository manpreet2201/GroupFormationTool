package CSCI5308.GroupFormationTool.resetpassword;

import CSCI5308.GroupFormationTool.AccessControl.IUserPersistence;
import CSCI5308.GroupFormationTool.AccessControl.User;
import CSCI5308.GroupFormationTool.Security.BCryptPasswordEncryption;
import CSCI5308.GroupFormationTool.Security.IPasswordEncryption;
import CSCI5308.GroupFormationTool.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class ResetPasswordController {
	
	private IResetPasswordService resetPasswordService;
	private IUserPersistence userDB;
	
	@Autowired
	private DefaultEmailService emailServiceImpl;
	
	public ResetPasswordController() {
		resetPasswordService = new DefaultResetPasswordService(
				new UserResetPasswordDAO(),
				new UserResetPasswordDB());
	}
	
	@GetMapping("/resetPassword")
	public String resetPasswordGet() {
		return "resetPassword";
	}
	
	@PostMapping("/resetPassword")
	public String resetPasswordPost(@RequestParam("bannerID") String bannerID,
			HttpServletRequest request,
			Model theModel) {

		System.out.print(bannerID);
		// look up user in database by bannerID
		userDB = SystemConfig.instance().getUserDB();
		User user = new User();
		userDB.loadUserByBannerID(bannerID, user);
		System.out.print(user.getEmail());
		if (!user.isValidUser())
		{
			theModel.addAttribute("message", "We didn't find an account for that e-mail address.");
		}
		else {
			theModel.addAttribute("message", "Reset password email has been sent.");
			// generate uuid used as reset_token
			user.setResetToken(UUID.randomUUID().toString().replace("-", ""));
			resetPasswordService.saveUserResetToken(user);
			emailServiceImpl.sendEmail(user, request);
		}
		
		return "messageDisplay";
	}
	
	@GetMapping("/reset_token/{param}")
	public String confirmPasswordGet(@PathVariable("param") String resetToken, Model theModel) {
		theModel.addAttribute("resetToken", resetToken);
		return "confirmPassword";
	}
	
	@PostMapping("/reset_token/{param}")
	public String confirmPasswordPost(@PathVariable("param") String resetToken,
			@RequestParam("password") String password,
			Model theModel) {
		
		User user = resetPasswordService.findUserByResetToken(resetToken);

		if(user!=null) {
			IPasswordEncryption passwordEncryption = new BCryptPasswordEncryption();
			password = passwordEncryption.encryptPassword(password);
			user.setPassword(password);
			resetPasswordService.saveUserPassword(user);
			theModel.addAttribute("message", "Password reset success!");
		}else {
			theModel.addAttribute("message", "Invalid Reset Token");
		}
		
		return "messageDisplay";
	}
	
}