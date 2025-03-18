package vacation.planner.cycle.entities;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cycle_information")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CycleInformation {
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(mappedBy = "cycleInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CycleEvent> events;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "user_name", nullable = false)
    private User user;

    public void setEvents(List<CycleEvent> events) {
        if (Objects.isNull(events)) {
            return;
        }
        this.events.clear();
        events.forEach(e -> e.setCycleInformation(this));
        this.events.addAll(events);
    }

}
