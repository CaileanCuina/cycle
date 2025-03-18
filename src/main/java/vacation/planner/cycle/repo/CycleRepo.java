package vacation.planner.cycle.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vacation.planner.cycle.entities.CycleInformation;

@Repository
public interface CycleRepo extends JpaRepository<CycleInformation, Long> {

    @Query("SELECT c FROM CycleInformation c JOIN c.user u WHERE ((:specificDate BETWEEN c.startDate AND c.endDate) or " +
            "(:specificDate >= c.startDate and c.endDate is null) or :specificDate = c.startDate or :specificDate = c.endDate)" +
            " AND u.userName = :username")
    List<CycleInformation> findCyclesByDateAndUsername(@Param("specificDate") LocalDate specificDate, @Param("username") String username);
}
