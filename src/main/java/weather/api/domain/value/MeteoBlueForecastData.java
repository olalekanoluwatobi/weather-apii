package weather.api.domain.value;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeteoBlueForecastData {
    @JsonProperty(value = "data_day")
    private DataDay dataDay;
    @JsonProperty(value = "data_1h")
    private DataOneHour dataOneHour;
}
