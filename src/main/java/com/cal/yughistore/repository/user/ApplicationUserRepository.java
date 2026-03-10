package com.cal.yughistore.repository.user;

import com.cal.yughistore.model.user.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    @Query("""
        select u from ApplicationUser u where trim(lower(u.credentials.email)) = :email
    """)
    Optional<ApplicationUser> findApplicationUserByEmail(@Param("email") String email);
}
