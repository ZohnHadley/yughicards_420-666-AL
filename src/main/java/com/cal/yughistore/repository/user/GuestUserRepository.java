package com.cal.yughistore.repository.user;

import com.cal.yughistore.model.user.GuestUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestUserRepository extends JpaRepository<GuestUser, Long> {
}
