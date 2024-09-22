package com.tech.center;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
	
	@Autowired
	private UserService service;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private AssignedOrdersService assignedOrdersService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private NotificationService notificationService;
	
	@GetMapping("/login")
	public String loginUser() {
		return "login";
	}
	
	@GetMapping("/register")
	public String signUpUser(Model model) {
		model.addAttribute("user", new User());
		return "signUp";
	}
	
	@GetMapping("/profile/{clientId}")
	public String userProfile(@PathVariable long clientId, Model model) {
		Optional<User> findUser = service.findUserById(clientId);
		User user = findUser.get();
		model.addAttribute("user", user);
		
		Profile profile = profileService.findProfileByUserId(user.getUserId());
		model.addAttribute("profile", profile);
		
		model.addAttribute("orders", orderService.findCurrentUserOrders(user.getUserId()));
		model.addAttribute("allorders", orderService.findAllOrders());
		model.addAttribute("assignedorders", assignedOrdersService.findCurrentUserAssignedOrders(user.getUserId()));
		model.addAttribute("reviews", reviewService.findAllReviews());
		
		return "profile";
	}
	
	@GetMapping("/freelancers")
	public String listOrderOngoing(Model model) {
		
		model.addAttribute("users", service.findAllUsers());
		model.addAttribute("profiles", profileService.findAllProfiles());
				
		return "freelancers";
	}
	
	@GetMapping("/user/notifications")
	public String listNotifications(@AuthenticationPrincipal UserDetailsPrincipal userDetails, Model model) {
		
		model.addAttribute("notifications", notificationService.findCurrentUserNotification(userDetails.getUserId()));
				
		return "notifications";
	}
	
	@GetMapping("/user/notifications/delete/{notificationId}")
	public String deleteNotification(@AuthenticationPrincipal UserDetailsPrincipal userDetails, @PathVariable Long notificationId, Model model) {
		
		notificationService.deleteNotification(notificationId);
		
		model.addAttribute("notifications", notificationService.findCurrentUserNotification(userDetails.getUserId()));
				
		return "notifications";
	}
	
	@GetMapping("/freelancers/sort/{type}/{field}")
	public String listOrderSorted(Model model, @PathVariable String field, @PathVariable String type) {
		model.addAttribute("users", service.findUsersWithSorting(field, type));
		model.addAttribute("profiles", profileService.findAllProfiles());
		return "freelancers";
	}
	
	@PostMapping("/process_register")
	public String processCreateUser(User user) {
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		service.saveUserAddProfile(user);
		return "index";
	}
	
	@PostMapping("/process_login")
	public String processAuthorization(@RequestParam String username, @RequestParam String password, Model model) {
		User user = service.authorize(username, password);
		if(user == null) {
			model.addAttribute("error", "Incorrect login or password");
			return "login";
		}
		model.addAttribute("user", user);
		return "index";
	}
	
	@PostMapping("/logout")
    public String logoutPage() {
        return "login";
    }
	

}
