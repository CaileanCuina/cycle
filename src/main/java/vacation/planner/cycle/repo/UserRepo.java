package vacation.planner.cycle.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vacation.planner.cycle.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

}
