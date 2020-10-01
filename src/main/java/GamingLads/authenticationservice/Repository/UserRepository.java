package GamingLads.authenticationservice.Repository;


import GamingLads.authenticationservice.Model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, String> {

}