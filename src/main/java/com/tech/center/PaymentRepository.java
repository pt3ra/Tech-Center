package com.tech.center;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

	@Query(value = "SELECT * FROM payment p WHERE p.order_id = :orderId", nativeQuery = true)
    Payment findByOrderId(@Param("orderId") Long orderId);
}
