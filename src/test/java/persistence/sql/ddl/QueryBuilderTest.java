package persistence.sql.ddl;

import domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persistence.sql.QueryBuilder;
import persistence.sql.dialect.Dialect;
import persistence.sql.dialect.H2Dialect;

import static org.assertj.core.api.Assertions.assertThat;

class QueryBuilderTest {

    private static final Logger logger = LoggerFactory.getLogger(QueryBuilderTest.class);

    private Dialect dialect;

    private QueryBuilder queryBuilder;

    @BeforeEach
    void setUp() {
        dialect = H2Dialect.getInstance();
        queryBuilder = new CreateQueryBuilder(dialect);
    }

    @Test
    @DisplayName("요구사항 3 - 추가된 정보를 통해 create 쿼리 만들어보기2")
    void generateCreateTableQuery_WithTableAndTransientAnnotation() {
        String createTableQuery = queryBuilder.generateQuery(Person.class);

        assertThat(createTableQuery).isEqualTo("CREATE TABLE users (id BIGINT NOT NULL AUTO_INCREMENT, nick_name VARCHAR, old INTEGER, email VARCHAR NOT NULL, PRIMARY KEY (id))");
    }

    @Test
    @DisplayName("요구사항 4 - 정보를 바탕으로 drop 쿼리 만들어보기")
    void generateDropTableQuery() {
        String dropTableQuery = queryBuilder.generateQuery(Person.class);

        assertThat(dropTableQuery).isEqualTo("DROP TABLE users");
    }

    @Test
    @DisplayName("요구사항 1 - insert 문 구현해보기")
    void generateInsertQuery() {
        String insertQuery = queryBuilder.generateInsertQuery(Person.of("crong", 35, "test@123.com"));

        assertThat(insertQuery).isEqualTo("INSERT INTO users (nick_name, old, email) VALUES (null, 'crong', 35, 'test@123.com')");
    }

    @Test
    @DisplayName("요구사항 2 - 위의 정보를 바탕으로 모두 조회(findAll) 기능 구현해보기")
    void generateSelectAllQuery() {
        String selectAllQuery = queryBuilder.generateSelectQuery(new Person(), null);

        assertThat(selectAllQuery).isEqualTo("SELECT id, nick_name, old, email FROM users");
    }

    @Test
    @DisplayName("요구사항 3 - 위의 정보를 바탕으로 단건 조회(findById) 기능 구현해보기")
    void generateSelectOneQuery() {
        String selectOneQuery = queryBuilder.generateSelectQuery(new Person(), 1L);

        assertThat(selectOneQuery).isEqualTo("SELECT id, nick_name, old, email FROM users WHERE id = 1");
    }

    @Test
    @DisplayName("요구사항 4 - delete 쿼리 만들어보기 (id가 없을 경우 전체 삭제)")
    void generateDeleteAllQuery() {
        String deleteQuery = queryBuilder.generateDeleteQuery(new Person(), null);

        assertThat(deleteQuery).isEqualTo("DELETE FROM users");
    }

    @Test
    @DisplayName("요구사항 4 - delete 쿼리 만들어보기 (id가 있을 경우 해당 id 삭제)")
    void generateDeleteOneQuery() {
        String deleteQuery = queryBuilder.generateDeleteQuery(new Person(), 1L);

        assertThat(deleteQuery).isEqualTo("DELETE FROM users WHERE id = 1");
    }
}
