package weather.api.domain.repository;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weather.api.domain.entity.ForecastDataDay;

import java.util.Optional;

@Repository
public interface ForecastDataDayRepository extends JpaRepository<ForecastDataDay,Long> {

    Optional<ForecastDataDay> findByTime(DateTime dateTime);

}
