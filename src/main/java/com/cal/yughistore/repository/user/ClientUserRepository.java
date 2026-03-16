package com.cal.yughistore.repository.user;

import com.cal.yughistore.model.user.ClientUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientUserRepository extends JpaRepository<ClientUser, Long> {
    boolean existsByCredentialsEmail(String email);

}
