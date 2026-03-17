package com.cal.yughistore.repository.user;

import com.cal.yughistore.model.user.ClientUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClientUserRepository extends JpaRepository<ClientUser, Long> {
    @Query("""
    select distinct c
    from ClientUser c
    left join fetch c.shoppingCart sc
    left join fetch sc.cartItemList
    where c.id = :id
""")
    Optional<ClientUser> findDetailedById(@Param("id") Long id);
    boolean existsById(Long id);
    boolean existsByCredentialsEmail(String email);
}
