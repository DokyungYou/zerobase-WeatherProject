package zerobase.weather.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JpaMemoRepositoryTest {
    @Autowired
    JpaMemoRepository jpaMemoRepository;

    @Test
     void insertMemoTest(){
     //given (parameter)
      Memo newMemo = new Memo(10,"this is jpa memo!");
     //when (method)
        jpaMemoRepository.save(newMemo);

     //then (assert)
        List<Memo> memoList = jpaMemoRepository.findAll();
        assertTrue(memoList.size() > 0);

    }

    @Test
     void findById(){
     //given (parameter)
        Memo newMemo = new Memo(11,"this is jpa!"); //사실 이 id는 의미가 없음 (@GeneratedValue(strategy = GenerationType.IDENTITY)해놨기때문에
     //when (method)
        Memo memo = jpaMemoRepository.save(newMemo);
        System.out.println(memo.getId());

        //then (assert)
//        Optional<Memo> result = jpaMemoRepository.findById(11);
        Optional<Memo> result = jpaMemoRepository.findById(memo.getId());
        assertEquals("this is jpa!", result.get().getText());
     }



}