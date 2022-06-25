package weather.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import weather.api.domain.service.ForecastDataService;
import weather.api.domain.value.MeteoBlueForecastData;
import weather.api.domain.value.WeatherResponsePayload;

@Controller
@RequestMapping("/api/v1/forecast")
public class ForecastDataController {
    @Autowired
    private ForecastDataService forecastDataService;

    /*
    though there is a cron job that loads data every day from meteoblue, this endpoint is to initiate the
    data load without having to wait for next job.
     */
    @GetMapping("loadData")
    public ResponseEntity<MeteoBlueForecastData> loadData() throws Exception {
        return new ResponseEntity<>(forecastDataService.loadMeteoData(), HttpStatus.OK);
    }

    @GetMapping("day")
    public ResponseEntity<WeatherResponsePayload> getWeatherReportForDay(@RequestParam String dateTime) throws Exception {
        return new ResponseEntity<>(forecastDataService.processPayloadForDateTime(dateTime, true), HttpStatus.OK);
    }

    @GetMapping("time")
    public ResponseEntity<WeatherResponsePayload> getWeatherReportForTime(@RequestParam String dateTime) throws Exception {
        return new ResponseEntity<>(forecastDataService.processPayloadForDateTime(dateTime, false), HttpStatus.OK);
    }

}
