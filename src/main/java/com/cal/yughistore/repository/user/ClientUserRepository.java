package com.cal.yughistore.repository.user;

import com.cal.yughistore.model.applicaitonuser.ClientUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientUserRepository extends JpaRepository<ClientUser, Long> {
    boolean existsByCredentialsEmail(String email);

}
