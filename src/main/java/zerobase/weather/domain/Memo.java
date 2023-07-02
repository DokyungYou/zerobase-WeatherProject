package zerobase.weather.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//db에 있는 테이블과 매칭 됨
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Memo") //연결할 테이블 이름 (테이블과 클래스이름 동일할땐 생략가능)
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본적인 key 생성을 db에 맡김 (스프링부트는 key 생성 X)
    private int id;
    private String text;
}
