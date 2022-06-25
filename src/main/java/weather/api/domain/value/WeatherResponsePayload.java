package weather.api.domain.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import weather.api.util.CustomDateTimeDeserializer;
import weather.api.util.CustomDateTimeSerializer;

@Getter
@Setter
public class WeatherResponsePayload {
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    DateTime dateTime;
    Double temperature;
    Double precipitation;
    Double windspeed;
    String shoe;
    String thingToUse;
    Double windChillPattern;

}
