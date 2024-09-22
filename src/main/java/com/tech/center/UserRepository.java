package com.tech.center;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByUsername(String username);
	
	@Query(value = "SELECT * FROM svuser c WHERE c.username = :username AND c.password = :password", nativeQuery = true)
    User authorizeUser(@Param("username") String username, @Param("password") String password);
	
}
