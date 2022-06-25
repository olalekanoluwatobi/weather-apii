package weather.api.domain.value;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataDay {

    private String[] time;
    private int[] pictocode;
    private int[] uvindex;
    private double[] temperatureMax;
    private double[] temperatureMin;
    @JsonProperty(value = "temperature_mean")
    private double[] temperatureMean;
    private double[] feltTemperatureMax;
    private double[] getFeltTemperatureMin;
    private int[] windDirection;
    private  int[] precipitationProbability;
    private  String[] rainspot;
    private int[] predictabilityClass;
    private int[] predictability;
    private double[] precipitation;
    private int[] snowfraction;
    private int[] sealevelpressurMax;
    private int[] sealevelpressurMin;
    private int[] sealevelpressureMean;
    private double[] windspeedMax;
    private double[] windspeedMin;
    @JsonProperty(value = "windspeed_mean")
    private double[] windspeedMean;
    private  int[] relativeHumidityMax;
    private int[] relativeHumidityMin;
    private int[] relativeHumidityMean;
    private int[] convectivePrecipitation;
    private int[] precipitationHours;
    private int[] humidityGreater90Hours;
    private String[] uvindexColor;
    private String[] temperatureMaxColor;
    private  String[] temperatureMaxFontColor;
    private String[] temperatureMInColor;
    private String[] temperatureMInFontColor;
    private String[] temperatureMeanColor;
    private String[] temperatureMeanFontColor;
    private  String[] feltTemperatureMaxColor;
    private  String[] feltTemperatureMaxFontColor;
    private  String[] feltTemperatureMinColor;
    private  String[] feltTemperatureMinFontColor;
    private String[] predictabilityClassColor;
    private String[] windSpeedMaxColor;
    private String[] windSpeedMinColor;
    private String[] windSpeedMeanColor;

}
