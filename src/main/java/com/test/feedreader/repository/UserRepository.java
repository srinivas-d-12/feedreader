package com.test.feedreader.repository;

import com.test.feedreader.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by sdhruvakumar.
 */

public interface UserRepository extends JpaRepository<User, Long> {

}
