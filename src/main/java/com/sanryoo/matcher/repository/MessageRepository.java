package com.sanryoo.matcher.repository;

import com.sanryoo.matcher.modal.MatcherMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<MatcherMessage, Long> {

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM messages " +
                    "WHERE (usersend = :usersend AND userreceive = :userreceive) OR (usersend = :userreceive AND userreceive = :usersend) " +
                    "ORDER BY messageid DESC " +
                    "LIMIT :start, :end"
    )
    List<MatcherMessage> getMessages(@Param("usersend") Long usersend, @Param("userreceive") Long userreceive, @Param("start") Long start, @Param("end") Long end);

}
