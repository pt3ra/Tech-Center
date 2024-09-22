package com.tech.center;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfileRepository extends JpaRepository<Profile, Long>{
	@Query(value = "SELECT * FROM profile p WHERE p.user_id = :userId", nativeQuery = true)
    Profile findProfileByUserId(@Param("userId") long userId);
}
