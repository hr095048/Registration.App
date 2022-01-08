package in.ashokit.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.ashokit.entities.UserEntity;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Serializable> {
	
	public String findByUserEmail(String userEmail);

}
