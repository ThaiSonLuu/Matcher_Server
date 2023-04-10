package com.sanryoo.matcher.repository;

import com.sanryoo.matcher.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByIdgoogle(String idGoogle);

    Optional<User> findByIdfacebook(String idFacebook);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM users WHERE " +
                    /*searching and sex is required first*/
                    "(searching = 1 AND sex <> :#{#u.sex} AND (" +
                    /*both is easy*/
                    "(easy = 1 AND :#{#u.easy} = 1)" +
                    " OR " +
                    /*other is easy and this user is not easy*/
                    "(easy = 1 AND status = :#{#u.status1} AND " +
                    "age >= :#{#u.age1} AND age <= :#{#u.age2} AND " +
                    "height >= :#{#u.height1} AND height <= :#{#u.height2} AND " +
                    "weight >= :#{#u.weight1} AND weight <= :#{#u.weight2} AND " +
                    "income >= :#{#u.income1} AND " +
                    "appearance >= :#{#u.appearance1})" +
                    " OR " +
                    /*this user is easy and other is not easy*/
                    "(:#{#u.easy} = 1 AND status1 = :#{#u.status} AND " +
                    "age1 <= :#{#u.age} AND age2 >= :#{#u.age} AND " +
                    "height1 <= :#{#u.height} AND height2 >= :#{#u.height} AND " +
                    "weight1 <= :#{#u.weight} AND weight2 >= :#{#u.weight} AND " +
                    "income1 <= :#{#u.income} AND " +
                    "appearance1 <= :#{#u.appearance})" +
                    " OR " +
                    /*both is not easy*/
                    "(status = :#{#u.status1} AND status1 = :#{#u.status} AND " +
                    "age >= :#{#u.age1} AND age <= :#{#u.age2} AND age1 <= :#{#u.age} AND age2 >= :#{#u.age} AND " +
                    "height >= :#{#u.height1} AND height <= :#{#u.height2} AND height1 <= :#{#u.height} AND height2 >= :#{#u.height} AND " +
                    "weight >= :#{#u.weight1} AND weight <= :#{#u.weight2} AND weight1 <= :#{#u.weight} AND weight2 >= :#{#u.weight} AND " +
                    "income >= :#{#u.income1} AND income1 <= :#{#u.income} AND " +
                    "appearance >= :#{#u.appearance1} AND appearance1 <= :#{#u.appearance})" +
                    ")" +
                    ")" +
                    "ORDER BY id ASC"
    )
    List<User> find(@Param("u") User user);
}
