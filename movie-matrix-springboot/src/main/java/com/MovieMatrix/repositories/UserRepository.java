package com.movieMatrix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import com.movieMatrix.models.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @NonNull
    Optional<User> findById(@NonNull Long id);

    //  Optional<User> findByname(String name);
    Optional<User> findByEmail(String email);

    //  Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);

    //  boolean existsByPhoneNumber(String phoneNumber);
//  boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);
    Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);

    void delete(@NonNull User entity);
//  void deleteAll(@NonNull Iterable<? extends User> entities);
    // void updateUser(User user);
}
