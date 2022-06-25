package weather.api.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import weather.api.util.CustomDateTimeDeserializer;
import weather.api.util.CustomDateTimeSerializer;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter
@Getter
public class ForecastDataOneHour{
    @Id
    private Long id;
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime time;
    private double temperature;

}