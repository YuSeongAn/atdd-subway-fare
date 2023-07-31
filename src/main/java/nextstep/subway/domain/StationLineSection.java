package nextstep.subway.domain;

import lombok.*;
import nextstep.subway.exception.StationLineSectionSplitException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Entity
@EqualsAndHashCode(of = "sectionId")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StationLineSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private StationLine line;

    @JoinColumn(name = "up_station_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Station upStation;

    @JoinColumn(name = "down_station_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Station downStation;

    @Column
    private BigDecimal distance;

    @Column
    private Long duration;

    @Builder
    public StationLineSection(Station upStation, Station downStation, BigDecimal distance, Long duration) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.duration = duration;
    }

    public void splitSection(Station newStation, Station standardStation, BigDecimal newSectionDistance, Long newDuration) {
        splitDistance(newSectionDistance);
        splitDuration(newDuration);

        if (standardStation.equals(upStation)) {
            this.upStation = newStation;
            return;
        }
        this.downStation = newStation;
    }

    private void splitDistance(BigDecimal newSectionDistance) {
        this.distance = distance.subtract(newSectionDistance);

        if (this.distance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new StationLineSectionSplitException("can't split existing section into larger distance section");
        }
    }

    private void splitDuration(Long newDuration) {
        this.duration = this.duration - newDuration;

        if (this.duration <= 0) {
            throw new StationLineSectionSplitException("can't split existing section into larger duration section");
        }
    }

    public Station getNewStation(List<Station> lineStations) {
        final boolean upStationExistingToLineStations = lineStations.stream()
                .anyMatch(upStation::equals);

        return upStationExistingToLineStations ? downStation : upStation;
    }

    public Station getStandardStation(List<Station> lineStations) {
        final boolean upStationExistingToLineStations = lineStations.stream()
                .anyMatch(upStation::equals);

        return upStationExistingToLineStations ? upStation : downStation;
    }

    public Long getUpStationId() {
        return this.upStation.getId();
    }

    public Long getDownStationId() {
        return this.downStation.getId();
    }

    //associate util method
    public void apply(StationLine line) {
        this.line = line;
    }
}
