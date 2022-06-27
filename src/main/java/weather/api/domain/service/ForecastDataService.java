package weather.api.domain.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import org.hibernate.annotations.NotFound;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mashape.unirest.http.HttpResponse;
import weather.api.domain.entity.ForecastDataDay;
import weather.api.domain.entity.ForecastDataOneHour;
import weather.api.domain.repository.ForecastDataDayRepository;
import weather.api.domain.repository.ForecastDataOneHourRepository;
import weather.api.domain.value.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ForecastDataService {

    @Value("${meteoblue.apiKey}")
    private String meteoblueApiKey;

    @Autowired
    private ForecastDataDayRepository forecastDataDayRepository;

    @Autowired
    private ForecastDataOneHourRepository forecastDataOneHourRepository;

    @Scheduled(cron = "0 0 23 * *") //run job at 23:00:00 every day
    public MeteoBlueForecastData loadMeteoData() throws Exception {
        String getMeetingsAPI = "http://my.meteoblue.com/packages/basic-1h_basic-day?lat=47.558&lon=7.573";

        final GetRequest httpRequestWithBody2 = Unirest.get(getMeetingsAPI);

        httpRequestWithBody2.queryString("apikey", meteoblueApiKey);


        final HttpResponse<String> stringHttpResponse = httpRequestWithBody2.asString();
        System.out.println("status" + stringHttpResponse.getStatus());
        System.out.println("status" + stringHttpResponse.getBody());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MeteoBlueForecastData meteoBlueForecastData = objectMapper.readValue(stringHttpResponse.getBody(), MeteoBlueForecastData.class);
        String[] dateStrings = meteoBlueForecastData.getDataDay().getTime();
        String[] timeStrings = meteoBlueForecastData.getDataOneHour().getTime();
        DataDay dataDay = meteoBlueForecastData.getDataDay();
        DataOneHour dataOneHour = meteoBlueForecastData.getDataOneHour();
        for (int i = 0; i < dateStrings.length - 1; i++) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime dt = formatter.parseDateTime(dateStrings[i]);

            Optional<ForecastDataDay> forecastDataDayOptional = forecastDataDayRepository.findByTime(dt);
            if (forecastDataDayOptional.isEmpty()) {
                ForecastDataDay forecastDataDay = new ForecastDataDay();
                forecastDataDay.setTime(dt);
                forecastDataDay.setTemperature(dataDay.getTemperatureMean()[i]);
                forecastDataDay.setWindspeed((dataDay.getWindspeedMean()[i])*3.6);
                forecastDataDay.setPrecipitation(dataDay.getPrecipitation()[i]);
                System.out.println("date: " + dateStrings[i] + "; temperature " + forecastDataDay.getTemperature() + "; windspeed " + forecastDataDay.getWindspeed() + "; precipitation " + forecastDataDay.getPrecipitation());
                forecastDataDayRepository.save(forecastDataDay);


            }

        }
        for (int i = 0; i < timeStrings.length - 1; i++) {
            DateTimeFormatter formatter1hr = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
            DateTime dt = formatter1hr.parseDateTime(timeStrings[i]);

            Optional<ForecastDataOneHour> forecastDataDayOptional = forecastDataOneHourRepository.findByTime(dt);
            if (forecastDataDayOptional.isEmpty()) {
                ForecastDataOneHour forecastDataOneHour = new ForecastDataOneHour();
                forecastDataOneHour.setTime(dt);
                forecastDataOneHour.setTemperature(dataOneHour.getTemperature()[i]);
                forecastDataOneHour.setWindspeed((dataOneHour.getWindspeed()[i])*3.6);
                forecastDataOneHour.setPrecipitation(dataOneHour.getConvectivePrecipitation()[i]);
                System.out.println("time " + timeStrings[i] + ";temperature " + forecastDataOneHour.getTemperature() + "; windspeed " + forecastDataOneHour.getWindspeed() + "; precipitation " + forecastDataOneHour.getPrecipitation());
                forecastDataOneHourRepository.save(forecastDataOneHour);


            }

        }

        return meteoBlueForecastData;
    }

    public List<WeatherResponsePayload> processDataForDateTimeInterval(String dateTime1,String dateTime2) throws Exception {
        ArrayList<WeatherResponsePayload> weatherResponsePayloads=new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime dt1 = formatter.parseDateTime(dateTime1);
        DateTime dt2=formatter.parseDateTime(dateTime2);
        List<ForecastDataDay> forecastDataDays  =forecastDataDayRepository.findAllByTimeBetween(dt1,dt2);
        for (ForecastDataDay forecastDataDay :
             forecastDataDays) {
            weatherResponsePayloads.add(createPayloadforDay(forecastDataDay));
        }
        return weatherResponsePayloads;
    }

    public WeatherResponsePayload processPayloadForDateTime(String dateTimeString, boolean isDay) throws Exception {


        if(isDay) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime dt = formatter.parseDateTime(dateTimeString);
            Optional<ForecastDataDay> forecastDataDayOptional=forecastDataDayRepository.findByTime(dt);
            if(forecastDataDayOptional.isEmpty()){
                throw new Exception("value not found");
            }
            ForecastDataDay forecastDataDay=forecastDataDayOptional.get();
            return createPayloadforDay(forecastDataDay);

        }
        else {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
            DateTime dt = formatter.parseDateTime(dateTimeString);
            Optional<ForecastDataOneHour> forecastDataOneHourOptional=forecastDataOneHourRepository.findByTime(dt);
            if(forecastDataOneHourOptional.isEmpty()){
                throw new Exception("value not found");
            }
            ForecastDataOneHour forecastDataOneHour=forecastDataOneHourOptional.get();
            return createPayloadForOneHour(forecastDataOneHour);
        }
    }
    private WeatherResponsePayload createPayloadforDay(ForecastDataDay forecastDataDay) throws Exception {




        WeatherResponsePayload weatherResponsePayload=new WeatherResponsePayload();
        weatherResponsePayload.setDateTime(forecastDataDay.getTime());
        weatherResponsePayload.setTemperature(forecastDataDay.getTemperature());
        weatherResponsePayload.setWindspeed(forecastDataDay.getWindspeed());
        weatherResponsePayload.setPrecipitation(forecastDataDay.getPrecipitation());
        Double precipitation= forecastDataDay.getPrecipitation();

        Double temperature=forecastDataDay.getTemperature();
        Shoe shoe=getShoe(temperature,precipitation);
        weatherResponsePayload.setShoe(shoe.toString());
        ThingsEnum thingsToUse=getThingsToUse(precipitation);
        weatherResponsePayload.setThingToUse(thingsToUse.toString());
        Double winchill=calculateWindChill(forecastDataDay.getWindspeed(),temperature);
        weatherResponsePayload.setWindChillPattern(winchill);

        return weatherResponsePayload;
    }

    private WeatherResponsePayload createPayloadForOneHour(ForecastDataOneHour forecastDataOneHour) throws Exception {

        WeatherResponsePayload weatherResponsePayload=new WeatherResponsePayload();
        weatherResponsePayload.setDateTime(forecastDataOneHour.getTime());
        weatherResponsePayload.setTemperature(forecastDataOneHour.getTemperature());
        weatherResponsePayload.setWindspeed(forecastDataOneHour.getWindspeed());
        weatherResponsePayload.setPrecipitation(forecastDataOneHour.getPrecipitation());
        Double precipitation= forecastDataOneHour.getPrecipitation();

        Double temperature=forecastDataOneHour.getTemperature();
        Shoe shoe=getShoe(temperature,precipitation);
        weatherResponsePayload.setShoe(shoe.toString());
        ThingsEnum thingsToUse=getThingsToUse(precipitation);
        weatherResponsePayload.setThingToUse(thingsToUse.toString());
        Double winchill=calculateWindChill(forecastDataOneHour.getWindspeed(),temperature);
        weatherResponsePayload.setWindChillPattern(winchill);
        return weatherResponsePayload;
    }
    private Shoe getShoe(Double temperature,Double precipitation) {
        boolean isRain=precipitation>5;
        if (temperature > 25) {
            return Shoe.SANDALS;
        } else if (temperature > 5 && temperature < 25) {
            if (!isRain) {
                return Shoe.LIGHT_BOOTS;
            } else {
                return Shoe.TALL_BOOTS;
            }
        } else {
            return Shoe.WINTER_SHOES;
        }
    }

    private ThingsEnum getThingsToUse(Double precipitation){
        if(precipitation>300){
            return ThingsEnum.BOAT;
        }else if(precipitation>5 &&precipitation<300){
            return ThingsEnum.UMBRELLA;
        }else {
            return ThingsEnum.HAT_OR_GLOVES;
        }
    }

    private double calculateWindChill(double windspeed,double temperature){
        Double windchill=0.0;
        if (windspeed >= 5.0){
            windchill=13.12+(0.6215*temperature)-(11.37*windspeed)+(0.3965*windspeed);
        }
        return windchill;
    }


    private void processMeteoBlueForecastData() {

    }

}
