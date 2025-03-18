package vacation.planner.cycle.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vacation.planner.cycle.entities.CycleEvent;


@Repository
public interface CycleEventRepo extends JpaRepository<CycleEvent, Long> {
}
