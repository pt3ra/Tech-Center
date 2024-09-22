package com.tech.center;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Long>{

	@Query(value = "SELECT * FROM role r WHERE r.rolename = :rolename", nativeQuery = true)
    Role getDefaultRole(@Param("rolename") String rolename);
}
