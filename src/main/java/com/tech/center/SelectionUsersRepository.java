package com.tech.center;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SelectionUsersRepository extends JpaRepository<SelectionUsers, Long>{

	@Query(value = "SELECT * FROM selectionusers s WHERE s.order_id = :orderId", nativeQuery = true)
    List<SelectionUsers> currentOrderSelection(@Param("orderId") Long orderId);
	
	@Transactional
    @Modifying
	@Query(value = "DELETE FROM selectionusers s WHERE s.order_id = :orderId", nativeQuery = true)
    void deleteAllByOrderId(@Param("orderId") Long orderId);
}
