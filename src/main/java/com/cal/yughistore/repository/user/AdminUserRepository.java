package com.cal.yughistore.repository.user;

import com.cal.yughistore.model.applicaitonuser.AdminUser;
import com.cal.yughistore.model.applicaitonuser.ClientUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    boolean existsByCredentialsEmail(String email);

}
