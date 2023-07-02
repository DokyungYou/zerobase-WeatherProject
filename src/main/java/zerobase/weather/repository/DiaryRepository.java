package zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Diary;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary,Integer>{

    // Chapter 05. 날씨 데이터 CRUD - 01_날씨 조회 API 작성 - 05:10~
    List<Diary> findAllByDate(LocalDate date);


    List<Diary> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    // 해당 날짜로 찾은 데이터 중 첫번째
    // getFirst == limit 1
    Diary getFirstByDate(LocalDate date);

    @Transactional  //
    void deleteAllByDate(LocalDate date);
}
