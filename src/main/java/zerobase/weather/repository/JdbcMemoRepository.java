package zerobase.weather.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zerobase.weather.domain.Memo;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository //스프링부트는 클래스이름만으로 Repository 로 인식 X
public class JdbcMemoRepository {
    private final JdbcTemplate jdbcTemplate;



    @Autowired  //Autowired 를 해줘야 데이터소스를 알아서 application.properties 에서 가져오게됨
    public JdbcMemoRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }





    //db 작업을 하기 위해선 memo 라는 객체를 저장, 조회를 할 수 있어야함

    public Memo save(Memo memo){
        String sql ="insert into memo values(?,?)";
        jdbcTemplate.update(sql, memo.getId(),memo.getText());
        return memo;
    }

    public List<Memo> findAll(){
        String sql = "select * from memo";
        //이 쿼리에는 반환을 해온 데이터값을 어떻게 반환을 할 것인지 써줘야함 -> memoRowMapper() -> Memo 객체로  반환
       return jdbcTemplate.query(sql, memoRowMapper());
    }


    //이 부분 다시 듣기 04_JDBC 방식으로 데이터 저장하기_2  21:00~
    //우리는 리턴해온 값의 id가 중복X, 하나씩 이라는 것을 알고 하나만 가져오려는 것을 알지만, 스프링부트는 그것을 모르기때문에 .findFirst()를 사용
    //만약 id가 3인 객체를 찾으려는데 3인 것이 없는 경우에 Optional이라는 객체로 랩핑해줌으로써 혹시 모를 null값을 처리하기 쉽게 해주는 자바 함수임
    public Optional<Memo> findById(int id){
        String sql = "select * from memo where id =?";
       return jdbcTemplate.query(sql, memoRowMapper(), id).stream().findFirst();
    }


    private RowMapper<Memo> memoRowMapper(){
        //jdbc 를 통해서 db 에서 데이터를 가져올때 그 데이터는 ResultSet 이라는 형식으로 가져오게 됨
        //ResultSet  ->   ex)  {id =1, text='this is memo~'} 의 형식
        //이 ResultSet 을 Memo 라는 형식로 매핑해줘야함 (이것을 RowMapper<>가 해줌 )

        //rs (resultSet)
        return (rs, rowNum) -> new Memo(
                rs.getInt("id"),
                rs.getString("text")
        );




    }

}
