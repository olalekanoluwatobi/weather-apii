package weather.api.domain.value;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataOneHour {
    private String[] time;
    private int[] uvindex;
    private int[] pictocode;
    private double[] temperature;
    private double[] feltTemperature;
    private int[] windDirection;
    @JsonProperty(value = "precipitation_probability")
    private  int[] precipitationProbability;
    private  String[] rainspot;
    private int[] sealevelpressure;
    private double[] windspeed;
    private  int[] relativeHumidity;
    @JsonProperty(value = "convective_precipitation")
    private int[] convectivePrecipitation;
    private int[] isDaylight;
}
