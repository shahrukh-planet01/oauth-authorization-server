package net.planet01.oauthauthorizationserver.repository;

import io.lettuce.core.dynamic.annotation.Param;
import net.planet01.oauthauthorizationserver.model.entity.Permission;
import net.planet01.oauthauthorizationserver.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.SqlResultSetMapping;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findUserByUsername(String u);
}
