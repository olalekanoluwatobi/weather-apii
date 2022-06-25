package weather.api.domain.repository;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weather.api.domain.entity.ForecastDataDay;
import weather.api.domain.entity.ForecastDataOneHour;

import java.util.Optional;

@Repository
public interface ForecastDataOneHourRepository extends JpaRepository<ForecastDataOneHour,Long> {
    Optional<ForecastDataOneHour> findByTime(DateTime dateTime);
}
