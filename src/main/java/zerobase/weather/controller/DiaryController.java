package zerobase.weather.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.domain.Diary;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

/*
Get방식 외의 요청은 왜 브라우저에서 path 를 입력하여 확인할 수 없는가?
ch04 - 02_날씨 데이터 저장 API 작성_2 12:00~

브라우저에 그냥 요청 url를 입력하는 것은 Get요청을 보내는 것으로 인식함
 기본적으로 브라우저는 캐싱을 한다
* */



//@RestController 란?  기본 Controller 기능 +  HTTP 응답을 보낼때 상태에 따라 상태코드를  Controller에서 지정해서 내려줄 수 있게끔 해줌
//상태코드: ex)  404, 200...
@RestController
public class DiaryController {
    private final DiaryService diaryService;


    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }




    //보통 조회를 할때는 Get, 저장할때는 Post 를 사용
    @ApiOperation(value = "날씨와 텍스트형식의 일기를 이용하여 DB에 일기 저장합니다.", notes = "노트 사용예시로 넣어봅니다.")
    @PostMapping("/create/diary")
    void createDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @ApiParam(value= "날짜 형식: YYYY-MM-DD", example = "2023-06-27")
            LocalDate date, //date 는 여러형식으로 저장될 수 있음

            @RequestBody String text
    ){
        diaryService.createDiary(date, text);

    }


    //하루 일기 조회
    @ApiOperation("선택한 날짜의 모든 일기 데이터를 가져옵니다.")
    @GetMapping("/read/diary")
    List<Diary> readDiary(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @ApiParam(value= "날짜 형식: YYYY-MM-DD", example = "2023-06-27")
            LocalDate date
   ){
       return diaryService.readDiary(date);

    }


    //특정날짜 범위의 일기 조회
    @ApiOperation("선택한 기간 중의 모든 일기 데이터를 가져옵니다.")
    @GetMapping("/read/diaries")
    List<Diary> readDiaries(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @ApiParam(value= "조회할 기간의 첫번째날", example = "2023-06-27")
            LocalDate startDate,

             @RequestParam
             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
             @ApiParam(value= "조회할 기간의 마지막날", example = "2023-06-27")
             LocalDate endDate
    ){
        return diaryService.readDiaries(startDate,endDate);

    }

    //다이어리 내용 수정 (해당 날짜의 첫번째 데이터만)
    @ApiOperation("선택한 날짜의 첫번째 일기내용을 수정합니다.")
    @PutMapping("/update/diary")
    void updateDiary(
             @RequestParam
             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
             @ApiParam(value= "날짜 형식: YYYY-MM-DD", example = "2023-06-27")
             LocalDate date,

             @RequestBody String text
    ){
        diaryService.updateDiary(date,text);
    }


    //다이어리 삭제
    @ApiOperation("선택한 날짜의 첫번째 일기내용을 삭제합니다.")
    @DeleteMapping("/delete/diary")
    void deleteDiary(
                @RequestParam
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                @ApiParam(value= "날짜 형식: YYYY-MM-DD", example = "2023-06-27")
                LocalDate date
    ){
        diaryService.deleteDiary(date);
    }


}
