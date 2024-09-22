package com.tech.center;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ProfileController {
	
	@Autowired
	private ProfileService service;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private AssignedOrdersService assignedOrdersService;
	
	@Autowired
	private ReviewService reviewService;
	
	@GetMapping("/profile/{clientId}/edit")
	public String userProfile(@AuthenticationPrincipal UserDetailsPrincipal userDetails, @PathVariable long clientId, Model model) {
		Optional<User> findUser = userService.findUserById(clientId);
		User user = findUser.get();
		model.addAttribute("user", user);
		model.addAttribute("allusers", userService.findAllUsers());
		
		Profile profile = service.findProfileByUserId(user.getUserId());
		model.addAttribute("profile", profile);
		
		model.addAttribute("orders", orderService.findCurrentUserOrders(user.getUserId()));
		model.addAttribute("allorders", orderService.findAllOrders());
		model.addAttribute("assignedorders", assignedOrdersService.findCurrentUserAssignedOrders(user.getUserId()));
		model.addAttribute("reviews", reviewService.findAllReviews());
		
		if(user.getUserId() != userDetails.getUserId()) {
			model.addAttribute("error", "No access to the profile");
			return "profile";
		}
		
		return "profileEdit";
	}
	
	@PostMapping("/process_edit_profile")
	public String processCreateUser(@AuthenticationPrincipal UserDetailsPrincipal userDetails, Profile profile, Model model) {
		Profile profileToUpdate = service.findProfileByUserId(userDetails.getUserId());
		
		profile.setProfileId(profileToUpdate.getProfileId());
		profile.setUserId(profileToUpdate.getUserId());
		
		service.saveProfile(profile);
		model.addAttribute("success", "Successfully updated");
		
		return "profileEdit";
	}
	

}
