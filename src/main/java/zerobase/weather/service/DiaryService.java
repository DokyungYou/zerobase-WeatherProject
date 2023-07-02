package zerobase.weather.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;  //javax.transaction 으로 import 하지 않게 주의
import zerobase.weather.WeatherApplication;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;

import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiaryService {


    //스프링부트에 이미 지정돼있는 변수들이 있고, 그 변수들의 하나인 openweathermap.key 라는 변수에 들어있는 값을 가져와서 apikey 라는 객체에 넣어주겠다는 의미
    //openweathermap.key는 application.properties 에 넣어놓은 상태 ( 그 값을 가져와서 쓰는 것임)
    @Value("${openweathermap.key}")
    private String apiKey;

    private final DiaryRepository diaryRepository;  //컨트롤러에서 서비스객체를 필드로 가져서 연결되듯이 서비스에선 레파지토리 연결
    private final DateWeatherRepository dateWeatherRepository;



    private static final Logger logger = LoggerFactory.getLogger(WeatherApplication.class);



    public DiaryService(DiaryRepository diaryRepository, DateWeatherRepository dateWeatherRepository){
        this.diaryRepository = diaryRepository;
        this.dateWeatherRepository = dateWeatherRepository;
    }


    // 데이터 캐싱
    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void saveWeatherDate(){
        logger.info("날씨 데이터 잘 가져옴");
        dateWeatherRepository.save(getWeatherFromApi());
    }



    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createDiary(LocalDate date, String text) {

        logger.info("started to created diary!");

        // 날씨 데이터 가져오기 (API에서 가져오기 or DB에서 가져오기)
        DateWeather dateWeather = getDateWeather(date);

        Diary nowDiary = new Diary();
        nowDiary.setDateWeather(dateWeather);
        nowDiary.setDate(date);
        nowDiary.setText(text);

        diaryRepository.save(nowDiary);

        logger.info("end to create diary!");


    }




    private DateWeather getWeatherFromApi(){
        //open weather map 에서 날씨 데이터 가져오기
        String weatherData = getWeatherString();

        //받아온 날씨 json 파싱하기
        Map<String, Object> parsedWeather = parseWeather(weatherData);

        DateWeather dateWeather  = new DateWeather();
        dateWeather.setDate(LocalDate.now());
        dateWeather.setWeather(parsedWeather.get("main").toString());
        dateWeather.setTemperature((double)parsedWeather.get("temp"));
        dateWeather.setIcon(parsedWeather.get("icon").toString());

        return  dateWeather;
    }


    private DateWeather getDateWeather(LocalDate date) {
       List<DateWeather> dateWeatherListFromDB = dateWeatherRepository.findAllByDate(date);
        if(dateWeatherListFromDB.size() == 0){
            // 새로 api에서 날씨 정보를 가져오기
            // 정책상 과거 날씨호출은 유료이기때문에 현재날씨 or 날씨없이 일기 저장
            return  getWeatherFromApi();
        }else {
            return  dateWeatherListFromDB.get(0);
        }
    }




    //하루 치 일기 조회
    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date) {
        // exceptionHandler넣어줘서 주석처리해놓음
//        if(date.isAfter(LocalDate.ofYearDay(2200,1))){
//            throw new InvalidDate();
//        }

        return diaryRepository.findAllByDate(date);

    }


    //특정날짜 범위의 일기 조회
    @Transactional(readOnly = true)
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {


       return diaryRepository.findAllByDateBetween(startDate, endDate);
    }


    //다이어리 내용 수정 (해당 날짜의 첫번째 데이터만)
    public void updateDiary(LocalDate date, String text) {
        Diary nowDiary = diaryRepository.getFirstByDate(date);
        nowDiary.setText(text); // 일기내용 갱신

        // 가져온 객체의 id 값은 그대로인 상태에서 save()한다면 덮어쓰기된다. (save는 새롭게 추가만 하는 것이 아님)
        diaryRepository.save(nowDiary);

    }

    //다이어리 삭제
    public void deleteDiary(LocalDate date) {

        diaryRepository.deleteAllByDate(date);
    }
    
    

    private String getWeatherString(){
       String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apiKey;


       try{
           URL url = new URL(apiUrl);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("GET");
           int responseCode = connection.getResponseCode();

           BufferedReader br;
           if(responseCode == 200){
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
           }else{
               br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
           }

           String inputLine;
           StringBuilder response = new StringBuilder();
           while((inputLine = br.readLine()) != null){
                response.append(inputLine);
           }
           br.close();

           return response.toString();
       }catch(Exception e){
           return "failed to get response";
       }

    }


    private Map<String, Object> parseWeather(String jsonString){

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try{
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        }catch (ParseException e){
            throw new RuntimeException(e);
        }

        Map<String, Object> resultMap = new HashMap<>();

        JSONObject mainData = (JSONObject) jsonObject.get("main");  // (JSONObject) 를 붙여줘야 JSONObject 형태로 인식됨
        resultMap.put("temp",mainData.get("temp"));


        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherData = (JSONObject) weatherArray.get(0); //0번째 객체 가져오기
        resultMap.put("main", weatherData.get("main"));
        resultMap.put("icon", weatherData.get("icon"));


       return resultMap;


    }


 
}
