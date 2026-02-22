package com.cal.yughistore.repository;

import com.cal.yughistore.model.applicaitonuser.ApplicationUser;
import com.cal.yughistore.model.applicaitonuser.ClientUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientUserRepository extends JpaRepository<ClientUser, Long> {
    boolean existsByCredentialsEmail(String email);

}
