package zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.weather.domain.Memo;


// jpa는 자바의 표준 orm
// 자바에서 orm 개념을 활용할 때 쓰이는 함수들은 JpaRepository 라는 곳에 다 정의돼있음
//이 형식으로 만들어준 것만으로도 JdbcMemoRepository 에 쓴 코드 다 생략 가능
@Repository
public interface JpaMemoRepository extends JpaRepository<Memo, Integer> { //Memo 클래스로 연결, 이 클래스의 key 형식

}
