package org.example.reopsitory;

import org.example.dtos.user.UserDTO;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByMail(String mail);

    @Query("select new org.example.dtos.user.UserDTO(u.id, u.isStudent) from User u " +
            "where u.mail = ?1 and u.password = ?2")
    UserDTO findUserByMail(String mail, String password);
}
