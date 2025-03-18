package vacation.planner.cycle.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cycle_event")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CycleEvent {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private CycleEventType type;

    private LocalDate occurrenceDate;

    @ManyToOne
    @JoinColumn(name = "cycle_information_id", nullable = false)
    private CycleInformation cycleInformation;

}
