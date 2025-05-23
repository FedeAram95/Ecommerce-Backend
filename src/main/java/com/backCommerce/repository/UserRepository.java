package com.backCommerce.repository;

import com.backCommerce.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :username AND u.password = :password")
    Optional<User> findByUsernameAndPasswordWithRoles(
        @Param("username") String username, 
        @Param("password") String password
    );
}
