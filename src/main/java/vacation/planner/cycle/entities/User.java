package vacation.planner.cycle.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    private String userName;

    private Double cycleStd;

    private Double avgCycleLen;

    private Double avgMensLen;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CycleInformation> cycleInformation = new ArrayList<>();

    public void setCycleInformation(List<CycleInformation> cycleInformation) {
        if (Objects.isNull(cycleInformation)) {
            return;
        }
        this.cycleInformation.clear();
        cycleInformation.forEach(c -> c.setUser(this));
        this.cycleInformation.addAll(cycleInformation);
    }

    public void updateFromUser(User userToUpdate){
        setCycleInformation(userToUpdate.getCycleInformation());
        this.avgCycleLen = userToUpdate.getAvgCycleLen();
        this.avgMensLen = userToUpdate.getAvgMensLen();
        this.cycleStd = userToUpdate.getCycleStd();
    }

}
