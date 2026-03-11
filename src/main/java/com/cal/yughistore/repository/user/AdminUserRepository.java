package com.cal.yughistore.repository.user;

import com.cal.yughistore.model.user.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    boolean existsByCredentialsEmail(String email);

}
