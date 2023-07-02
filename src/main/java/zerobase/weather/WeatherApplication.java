package zerobase.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class WeatherApplication {  //이 클래스 자체가 스프링부트 어플리케이션 큰 단위를 일컫는다.

    public static void main(String[] args) {
        SpringApplication.run(WeatherApplication.class, args);
    }

}
