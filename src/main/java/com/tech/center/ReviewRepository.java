package com.tech.center;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long>{

	@Query(value = "SELECT * FROM review r WHERE r.order_id = :orderId", nativeQuery = true)
    Review findByOrderId(@Param("orderId") Long orderId);
}
