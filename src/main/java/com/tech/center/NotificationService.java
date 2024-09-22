package com.tech.center;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
	
	@Autowired
	private NotificationRepository repository;
		// default output
	public List<Notification> findAllNotifications(){
		return repository.findAll();
	}
	
	public List<Notification> findCurrentUserNotification(Long userId){
		return repository.currentUserNotification(userId);
	}
	
		// default input
	public void saveNotification(Notification notification) {
		
		repository.save(notification);
	}
	
	// delete
	public void deleteNotification(Long notification_id) {
		
		repository.deleteById(notification_id);
	}
	
	public void deleteAllNotifications(Long user_id) {
		
		repository.deleteAllByUserId(user_id);
	}
		
}
