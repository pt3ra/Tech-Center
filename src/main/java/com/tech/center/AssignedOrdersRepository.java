package com.tech.center;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssignedOrdersRepository extends JpaRepository<AssignedOrders, Long>{

	@Query(value = "SELECT * FROM assignedorders a WHERE a.user_id = :userId", nativeQuery = true)
    List<AssignedOrders> currentUserOrders(@Param("userId") Long userId);
	
	@Query(value = "SELECT * FROM assignedorders a WHERE a.order_id = :orderId", nativeQuery = true)
    AssignedOrders currentOrderOrders(@Param("orderId") Long orderId);
	
}
