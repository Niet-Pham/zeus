package vn.tech.website.store.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDoughnut {
    private Integer total;
    private Integer totalWaiting;
    private Integer totalPlaying;
    private Integer totalEnded;
    private Integer totalCanceled;
}
