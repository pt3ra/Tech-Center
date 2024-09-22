package com.tech.center;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itextpdf.text.DocumentException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import jakarta.validation.Valid;


@Controller
public class OrderController {
	
	@Autowired
	private OrderService service;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AssignedOrdersService assignedOrdersService;
	
	@Autowired
	private SelectionUsersService selectionService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private SolutionService solutionService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private ReviewService reviewService;
	
	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
	
	@GetMapping("/order/create")
	public String createOrder(Model model) {
		model.addAttribute("order", new Order());
		return "projectCreation";
	}
	
	
	@PostMapping("/process_order_creation")
	public String processCreateOrder(@AuthenticationPrincipal UserDetailsPrincipal userDetails, Order order) {
		Optional<User> findUser = userService.findUserById(userDetails.getUserId());
		User user = findUser.get();
		if(user == null) //ERROR
			return "index";
		
		service.saveOrder(user, order);
		return "index";
	}
	
	@GetMapping("/order/list/all")
	public String listOrderAll(Model model) {
		model.addAttribute("orders", service.findAllOrders());
		
		model.addAttribute("users", userService.findAllUsers());
		
		return "projectList";
	}
	
	@GetMapping("/order/ongoing")
	public String listOrderOngoing(@AuthenticationPrincipal UserDetailsPrincipal userDetails, Model model) {
		
		List<Order> ordersList = service.findIncompleteOrders();
		List<AssignedOrders> assignedOrdersList = assignedOrdersService.findCurrentUserAssignedOrders(userDetails.getUserId());
		
		Set<Long> orderIdsInAssignedOrders = new HashSet<>();
		for (AssignedOrders assignedOrder : assignedOrdersList) {
		  orderIdsInAssignedOrders.add(assignedOrder.getOrderId());
		}

		List<Order> matchingOrders = new ArrayList<>();
		for (Order order : ordersList) {
		  if (orderIdsInAssignedOrders.contains(order.getOrderId())) {
		    matchingOrders.add(order);
		  }
		}
		
		model.addAttribute("assignedorders", matchingOrders);
		model.addAttribute("orders", service.findCurrentUserOrders(userDetails.getUserId()));
				
		return "projectOngoing";
	}
	
	// JSON response
	@GetMapping(path = "/order/list/all", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<Order>> listOrderAllJson() {
        List<Order> orders = service.findAllOrders();
        return ResponseEntity.ok().body(orders);
    }
	
	// PDF generation
	@GetMapping("/order/list/pdf")
    public ResponseEntity<byte[]> generatePdf() {
        try {
            List<Order> orders = service.findAllOrders();
            byte[] pdfBytes = PdfService.generatePdfDocument(orders, "orders");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "order_list.pdf");

            return new ResponseEntity<>(pdfBytes, headers, org.springframework.http.HttpStatus.OK);
        } catch (DocumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	@GetMapping("/order/list/sort/{type}/{field}")
	public String listOrderSorted(Model model, @PathVariable String field, @PathVariable String type) {
		model.addAttribute("orders", service.findOrderWithSorting(field, type));
		model.addAttribute("users", userService.findAllUsers());
		return "projectList";
	}
	
	@GetMapping("/order/list/pagination/{offset}/{pageSize}")
	public String listOrderPaginated(Model model, @PathVariable int offset, @PathVariable int pageSize) {
		Page<Order> ordersWithPagination = service.findOrderWithPagination(offset, pageSize);
		model.addAttribute("orders", ordersWithPagination);
		model.addAttribute("users", userService.findAllUsers());
		return "projectList";
	}
	
	@GetMapping("/order/list/pagination/{offset}/{pageSize}/sort/{type}/{field}")
	public String listOrderPaginatedandSorted(Model model, @PathVariable int offset, @PathVariable int pageSize, @PathVariable String field, @PathVariable String type) {
		Page<Order> ordersWithPagination = service.findOrderWithPaginatiobAndSorting(offset, pageSize, field, type);
		model.addAttribute("orders", ordersWithPagination);
		model.addAttribute("users", userService.findAllUsers());
		return "projectList";
	}
	
	@GetMapping("/order/list/{keyword}")
	public String listOrderByKeyword(Model model, @PathVariable String keyword) {
		model.addAttribute("orders", service.findOrderByKeyword(keyword));
		model.addAttribute("users", userService.findAllUsers());
		return "projectList";
	}

	//To Order page
	@GetMapping("/order/view/{orderId}")
	public String orderPage(@PathVariable long orderId, @AuthenticationPrincipal UserDetailsPrincipal userDetails, Model model) {
		Optional<Order> findOrder = service.findOrderById(orderId);
		Order order = findOrder.get();
		model.addAttribute("order", order);
		
		if(userDetails == null) //ERROR
			return "order";
		
		List<SelectionUsers> selectionList = selectionService.findCurrentOrderSelection(orderId);
		for (SelectionUsers selectionUsers : selectionList) {
			  if (selectionUsers.getUserId() == userDetails.getUserId()) {
				  model.addAttribute("selection", selectionUsers);
				  break;
			  }
			}
		
		return "order";
	}
	
	//Assign to order
	@GetMapping("/order/assign/{orderId}")
	public String orderAssignment(@PathVariable long orderId, @AuthenticationPrincipal UserDetailsPrincipal userDetails, Model model) {
		Optional<Order> findOrder = service.findOrderById(orderId);
		Order order = findOrder.get();
		model.addAttribute("order", order);
		
		List<SelectionUsers> selectionList = selectionService.findCurrentOrderSelection(order.getOrderId());
		for (SelectionUsers selectionUsers : selectionList) { //ERROR
			  if (selectionUsers.getUserId() == userDetails.getUserId()) {
				  model.addAttribute("selection", selectionUsers);
				  return "order";
			  }
		}
		
		Optional<User> findUser = userService.findUserById(userDetails.getUserId());
		User user = findUser.get();
		
		SelectionUsers selectionUsers = new SelectionUsers(order.getOrderId(), user);
		selectionService.saveAssignedUser(selectionUsers);
		model.addAttribute("selection", selectionUsers);
		
		final String message = "Користувач " + userDetails.getFirstName() + " " + userDetails.getLastName() + " вiдгукнувся на ваше оголошення: " + order.toString();
		Notification notification = new Notification(message, order.getOrderId(), order.getUserId());
		notificationService.saveNotification(notification);
		
		return "order";
	}
	
	//Select order employee
	@GetMapping("/order/process/{orderId}")
	public String orderSelectionUser(@PathVariable long orderId, @AuthenticationPrincipal UserDetailsPrincipal userDetails, Model model) {
		Optional<Order> findOrder = service.findOrderById(orderId);
		Order order = findOrder.get();
		model.addAttribute("order", order);
		
		model.addAttribute("selections", selectionService.findCurrentOrderSelection(order.getOrderId()));
		
		if(order.getUserId() != userDetails.getUserId()) { //ERROR
			return "order";
		}
		
		model.addAttribute("profiles", profileService.findAllProfiles());
		
		return "selection";
	}
	
	@GetMapping("/order/start/{orderId}/{userId}")
	public String orderSelectUser(@PathVariable long orderId, @PathVariable long userId, @AuthenticationPrincipal UserDetailsPrincipal userDetails, Model model) {
		Optional<Order> findOrder = service.findOrderById(orderId);
		Order order = findOrder.get();
		
		Optional<User> findUser = userService.findUserById(userId);
		User user = findUser.get();
		model.addAttribute("user", user);
		
		
		if(order.getUserId() != userDetails.getUserId()) { //ERROR
			model.addAttribute("order", order);
			model.addAttribute("selections", selectionService.findCurrentOrderSelection(order.getOrderId()));
			return "order";
		}
		
		model.addAttribute("profiles", profileService.findAllProfiles());
		
		selectionService.deleteOrderSelection(order.getOrderId());
		
		AssignedOrders assignedOrder = new AssignedOrders(order.getOrderId(), user.getUserId());
		assignedOrdersService.saveAssignedOrder(assignedOrder);
		
		order.setStatus("Assigned");
		service.saveOrderDefault(order);
		model.addAttribute("order", order);
		
		final String message = "Користувач " + userDetails.getFirstName() + " " + userDetails.getLastName() + " призначив вас на виконання свого оголошення: " + order.toString();
		Notification notification = new Notification(message, order.getOrderId(), user.getUserId());
		notificationService.saveNotification(notification);
		
		// REDIRECT TO NEW FUNCTIONALITY
		
		AssignedOrders assignedOrders = assignedOrdersService.findCurrentOrderAssignedOrders(order.getOrderId());
		
		Optional<User> findAssignedUser = userService.findUserById(assignedOrders.getUserId());
		User assignedUser = findAssignedUser.get();
		model.addAttribute("assigneduser", assignedUser);
		
		Optional<User> findCreatorUser = userService.findUserById(order.getUserId());
		User creatorUser = findCreatorUser.get();
		model.addAttribute("creatoruser", creatorUser);
		
		model.addAttribute("profiles", profileService.findAllProfiles());
		
		return "orderProcess";
	}
	
	@GetMapping("/order/processing/{orderId}")
	public String orderProcessing(@PathVariable long orderId, @AuthenticationPrincipal UserDetailsPrincipal userDetails, @RequestParam(value = "payment_intent", required = false) String paymentIntent, @RequestParam(value = "payment_intent_client_secret", required = false) String paymentIntentClientSecret, Model model) {
		Optional<Order> findOrder = service.findOrderById(orderId);
		Order order = findOrder.get();
		model.addAttribute("order", order);
		
		AssignedOrders assignedOrders = assignedOrdersService.findCurrentOrderAssignedOrders(order.getOrderId());
		
		if(order.getUserId() != userDetails.getUserId() && assignedOrders.getUserId() != userDetails.getUserId()) { //ERROR
			return "order";
		}
		
		Optional<User> findAssignedUser = userService.findUserById(assignedOrders.getUserId());
		User assignedUser = findAssignedUser.get();
		model.addAttribute("assigneduser", assignedUser);
		
		Optional<User> findCreatorUser = userService.findUserById(order.getUserId());
		User creatorUser = findCreatorUser.get();
		model.addAttribute("creatoruser", creatorUser);
		
		model.addAttribute("profiles", profileService.findAllProfiles());
		
		if(paymentIntent != null) { // Add more sensitive checks in future && order.getPayment() == null
			Payment payment = new Payment(paymentIntent, paymentIntentClientSecret, order);
			paymentService.savePayment(payment);
			
			payment = paymentService.findPaymentByOrderId(order.getOrderId());
			
			// UPDATING ORDER
			order.setPayment(payment);
			service.saveOrderDefault(order);
			
			return "orderProcess";
		}
		
		
		return "orderProcess";
	}
	
	// TO SOLUTION PAGE
	@GetMapping("/order/solution/{orderId}")
	public String orderSuggestSolution(@PathVariable long orderId, @AuthenticationPrincipal UserDetailsPrincipal userDetails, Model model) {
		Optional<Order> findOrder = service.findOrderById(orderId);
		Order order = findOrder.get();
		model.addAttribute("order", order);
		
		AssignedOrders assignedOrders = assignedOrdersService.findCurrentOrderAssignedOrders(order.getOrderId());
		
		if(order.getUserId() != userDetails.getUserId() && assignedOrders.getUserId() != userDetails.getUserId()) { //ERROR
			return "order";
		}
		
		Optional<User> findAssignedUser = userService.findUserById(assignedOrders.getUserId());
		User assignedUser = findAssignedUser.get();
		model.addAttribute("assigneduser", assignedUser);
		
		Optional<User> findCreatorUser = userService.findUserById(order.getUserId());
		User creatorUser = findCreatorUser.get();
		model.addAttribute("creatoruser", creatorUser);
		
		Solution solution = new Solution();
		model.addAttribute("solution", solution);
		
		return "solution";
	}
	
	// SEND SOLUTION
	@PostMapping("/order/sendsolution/{orderId}")
	public String orderSendSolution(@PathVariable long orderId, @AuthenticationPrincipal UserDetailsPrincipal userDetails, Solution solution, Model model) {
		Optional<Order> findOrder = service.findOrderById(orderId);
		Order order = findOrder.get();
			
		AssignedOrders assignedOrders = assignedOrdersService.findCurrentOrderAssignedOrders(order.getOrderId());
			
		if(assignedOrders.getUserId() != userDetails.getUserId()) { //ERROR
			model.addAttribute("order", order);
			return "order";
		}
			
		Optional<User> findAssignedUser = userService.findUserById(assignedOrders.getUserId());
		User assignedUser = findAssignedUser.get();
		model.addAttribute("assigneduser", assignedUser);
			
		Optional<User> findCreatorUser = userService.findUserById(order.getUserId());
		User creatorUser = findCreatorUser.get();
		model.addAttribute("creatoruser", creatorUser);
			
		// UPDATING SOLUTION
		Solution hasSolution = solutionService.findSolutionByOrderId(order.getOrderId());
		if(hasSolution != null){
			solution.setSolutionId(hasSolution.getSolutionId());
		}
			
		// SAVING SOLUTION
		solution.setOrder(order);
		solutionService.saveSolution(solution);
		model.addAttribute("solution", solution);
			
		solution = solutionService.findSolutionByOrderId(order.getOrderId());
			
		// UPDATING ORDER
		order.setSolution(solution);
		order.setSolutionStatus("done");
		service.saveOrderDefault(order);
		model.addAttribute("order", order);
		model.addAttribute("success", "Successfully added");
			
		return "solution";
	}
		
	// REQUEST FOR SOLUTION UPDATE
	@GetMapping("/order/undonesolution/{orderId}")
	public String orderUndoneSolution(@PathVariable long orderId, @AuthenticationPrincipal UserDetailsPrincipal userDetails, Solution solution, Model model) {
		Optional<Order> findOrder = service.findOrderById(orderId);
		Order order = findOrder.get();
			
		AssignedOrders assignedOrders = assignedOrdersService.findCurrentOrderAssignedOrders(order.getOrderId());
			
		if(order.getUserId() != userDetails.getUserId()) { //ERROR
			model.addAttribute("order", order);
			return "order";
		}
			
		Optional<User> findAssignedUser = userService.findUserById(assignedOrders.getUserId());
		User assignedUser = findAssignedUser.get();
		model.addAttribute("assigneduser", assignedUser);
			
		Optional<User> findCreatorUser = userService.findUserById(order.getUserId());
		User creatorUser = findCreatorUser.get();
		model.addAttribute("creatoruser", creatorUser);
			
		// UPDATING ORDER
		order.setSolutionStatus("undone");
		service.saveOrderDefault(order);
		model.addAttribute("order", order);
		model.addAttribute("success", "Solution declined");
			
		return "solution";
	}
	
	
	@Value("${stripe.api.publicKey}")
	private String publicKey;
	
	//TO PAYMENT PAGE
	@GetMapping("/order/payment/{orderId}")
	public String orderTriggerPayment(@PathVariable long orderId, @AuthenticationPrincipal UserDetailsPrincipal userDetails, Model model) {
		Optional<Order> findOrder = service.findOrderById(orderId);
		Order order = findOrder.get();
			
		AssignedOrders assignedOrders = assignedOrdersService.findCurrentOrderAssignedOrders(order.getOrderId());
			
		if(order.getUserId() != userDetails.getUserId()) { //ERROR
			model.addAttribute("order", order);
			return "order";
		}
			
		Optional<User> findAssignedUser = userService.findUserById(assignedOrders.getUserId());
		User assignedUser = findAssignedUser.get();
		model.addAttribute("assigneduser", assignedUser);
			
		Optional<User> findCreatorUser = userService.findUserById(order.getUserId());
		User creatorUser = findCreatorUser.get();
		model.addAttribute("creatoruser", creatorUser);
			
		model.addAttribute("order", order);
		
		Profile profile = profileService.findProfileByUserId(order.getUser().getUserId());
		
		Request request = new Request();
		request.setAmount((long) order.getBudget());
		request.setProductName(order.getOrderName());
		request.setEmail(profile.getEmail());
		
		model.addAttribute("request", request);
			
		return "payment";
	}
	
	// PAYMENT FORM SUBMIT
	@PostMapping("/order/process_payment/{orderId}")
	public String showCard(@ModelAttribute @Valid Request request, @AuthenticationPrincipal UserDetailsPrincipal userDetails, @PathVariable long orderId, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) { //ERROR
			return "payment";
		}
		Optional<Order> findOrder = service.findOrderById(orderId);
		Order order = findOrder.get();
		
		if(order.getUserId() != userDetails.getUserId()) { //ERROR
			model.addAttribute("order", order);
			return "order";
		}
		
		model.addAttribute("publicKey", publicKey);
		model.addAttribute("amount", request.getAmount());
		model.addAttribute("email", request.getEmail());
		model.addAttribute("productName", request.getProductName());
		
		return "checkout";
	}
	
	// REQUEST FOR REVIEW
	@GetMapping("/order/review/{orderId}")
	public String orderReview(@PathVariable long orderId, @AuthenticationPrincipal UserDetailsPrincipal userDetails, Solution solution, Model model) {
		Optional<Order> findOrder = service.findOrderById(orderId);
		Order order = findOrder.get();
		model.addAttribute("order", order);
			
		if(order.getUserId() != userDetails.getUserId()) { //ERROR
			return "order";
		}
			
		Optional<User> findCreatorUser = userService.findUserById(order.getUserId());
		User creatorUser = findCreatorUser.get();
		model.addAttribute("creatoruser", creatorUser);
		
		Review review = new Review();
		model.addAttribute("review", review);
			
		return "review";
	}
		
	// SEND REVIEW
	@PostMapping("/order/sendreview/{orderId}")
	public String orderSendReview(@PathVariable long orderId, @AuthenticationPrincipal UserDetailsPrincipal userDetails, Review review, Model model) {
		Optional<Order> findOrder = service.findOrderById(orderId);
		Order order = findOrder.get();
				
		AssignedOrders assignedOrders = assignedOrdersService.findCurrentOrderAssignedOrders(order.getOrderId());
				
		if(order.getUserId() != userDetails.getUserId()) { //ERROR
			model.addAttribute("order", order);
			return "order";
		}
				
		Optional<User> findAssignedUser = userService.findUserById(assignedOrders.getUserId());
		User assignedUser = findAssignedUser.get();
			
		Optional<User> findCreatorUser = userService.findUserById(order.getUserId());
		User creatorUser = findCreatorUser.get();
		model.addAttribute("creatoruser", creatorUser);
				
		// SAVING REVIEW
		review.setOrder(order);
		reviewService.saveReview(review);
		model.addAttribute("review", review);
				
		review = reviewService.findReviewByOrderId(order.getOrderId());
				
		// UPDATING ORDER
		order.setReview(review);
		service.saveOrderDefault(order);
		model.addAttribute("order", order);
		
		// UPDATING USER
		assignedUser.updateCount();
		assignedUser.updateSum(review.getGrade());
		userService.saveUser(assignedUser);
		model.addAttribute("assigneduser", assignedUser);
				
		return "orderProcess";
	}
	
	// SET FINISHED
	@GetMapping("/order/finishsolution/{orderId}")
	public String orderFinishSolution(@PathVariable long orderId, @AuthenticationPrincipal UserDetailsPrincipal userDetails, Model model) {
		Optional<Order> findOrder = service.findOrderById(orderId);
		Order order = findOrder.get();
			
		AssignedOrders assignedOrders = assignedOrdersService.findCurrentOrderAssignedOrders(order.getOrderId());
			
		if(assignedOrders.getUserId() != userDetails.getUserId()) { //ERROR
			model.addAttribute("order", order);
			return "order";
		}
			
		Optional<User> findAssignedUser = userService.findUserById(assignedOrders.getUserId());
		User assignedUser = findAssignedUser.get();
		model.addAttribute("assigneduser", assignedUser);
			
		Optional<User> findCreatorUser = userService.findUserById(order.getUserId());
		User creatorUser = findCreatorUser.get();
		model.addAttribute("creatoruser", creatorUser);
			
		// UPDATING ORDER
		order.setSolutionStatus("finished");
		service.saveOrderDefault(order);
		model.addAttribute("order", order);
			
		return "orderProcess";
	}
	
	// COMPLETE ORDER
	@GetMapping("/order/finish/{orderId}")
	public String orderComplete(@PathVariable long orderId, @AuthenticationPrincipal UserDetailsPrincipal userDetails, Model model) {
		Optional<Order> findOrder = service.findOrderById(orderId);
		Order order = findOrder.get();
			
		AssignedOrders assignedOrders = assignedOrdersService.findCurrentOrderAssignedOrders(order.getOrderId());
			
		if(order.getUserId() != userDetails.getUserId()) { //ERROR
			model.addAttribute("order", order);
			return "order";
		}
			
		Optional<User> findAssignedUser = userService.findUserById(assignedOrders.getUserId());
		User assignedUser = findAssignedUser.get();
		model.addAttribute("assigneduser", assignedUser);
			
		Optional<User> findCreatorUser = userService.findUserById(order.getUserId());
		User creatorUser = findCreatorUser.get();
		model.addAttribute("creatoruser", creatorUser);
			
		// UPDATING ORDER
		order.setStatus("Completed");
		service.saveOrderDefault(order);
		model.addAttribute("order", order);
			
		return "orderProcess";
	}

	
	
	// Search
	@RequestMapping(path = {"/order/view/search"}) // (path = {"/", "/search"})
	public String listOrderBySearchBar(Model model, String keyword) {
		if(keyword == null) {
			model.addAttribute("orders", service.findAllOrders());
		}

		model.addAttribute("orders", service.findOrderByKeyword(keyword));
		model.addAttribute("users", userService.findAllUsers());
		return "projectList";
	}
	
	//PDF Generation
		@GetMapping({"/order/list/{keyword}/pdf", "/pdf"})
	    public ResponseEntity<byte[]> generatePdf(@PathVariable(required = false) String keyword, @RequestParam(required = false) String searchKeyword) {
	        try {
	        	String effectiveKeyword = keyword;
	        	if(searchKeyword != null) effectiveKeyword = searchKeyword;
	        	
	            List<Order> orders = service.findOrderByKeyword(effectiveKeyword);
	            byte[] pdfBytes = PdfService.generatePdfDocument(orders, "orders with keyword=" + keyword);

	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_PDF);
	            headers.setContentDispositionFormData("attachment", "order_list.pdf");

	            return new ResponseEntity<>(pdfBytes, headers, org.springframework.http.HttpStatus.OK);
	        } catch (DocumentException e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

}
