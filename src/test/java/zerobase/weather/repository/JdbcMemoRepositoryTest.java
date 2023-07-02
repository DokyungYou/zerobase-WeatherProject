package zerobase.weather.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest  //test 라고 명시해줘야함
@Transactional //db 테스트를 하면서 실제 db에 있는 데이터가 변경되는 것을 방지하기 위함
class JdbcMemoRepositoryTest {

    @Autowired
    JdbcMemoRepository jdbcMemoRepository;

    @Test
     void insertMemoTest(){
     //given (parameter)
      Memo newMemo = new Memo(2,"insertMemoTest!");


      //when (method)
      jdbcMemoRepository.save(newMemo);

     //then (assert)
        Optional<Memo> result = jdbcMemoRepository.findById(2);
        assertEquals("insertMemoTest!", result.get().getText());

     }


     @Test
      void findAllMemoTest(){
      //given (parameter)
      //when (method)
         List<Memo> memoList = jdbcMemoRepository.findAll();
         //then (assert)
         System.out.println(memoList);
         assertNotNull(memoList);

      }

}