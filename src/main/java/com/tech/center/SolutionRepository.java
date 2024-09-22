package com.tech.center;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SolutionRepository extends JpaRepository<Solution, Long>{

	@Query(value = "SELECT * FROM solution s WHERE s.order_id = :orderId", nativeQuery = true)
    Solution findByOrderId(@Param("orderId") Long orderId);
}
