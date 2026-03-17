package com.cal.yughistore.repository.user;

import com.cal.yughistore.model.user.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findByCredentialsEmail(String email);
    ApplicationUser getById(Long id);
    @Query("""
        select u from ApplicationUser u where trim(lower(u.credentials.email)) = trim(lower(:email))
    """)
    Optional<ApplicationUser> findApplicationUserByEmail(@Param("email") String email);

}