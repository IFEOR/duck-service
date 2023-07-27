package autotests.tests.crud;

import autotests.clients.DuckCrudClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.*;

public class GetAllIdsTest extends DuckCrudClient {

    @Test(description = "Проверка, что отображается список нескольких уточек", priority = 1)
    @CitrusTest
    public void getFewIdsTest(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner, "${id1}");
        finallyDuckDelete(runner, "${id2}");
        finallyDuckDelete(runner, "${id3}");
        CleanDB(runner);
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "id1");
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "id2");
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "id3");
        getAllIds(runner);
        validateResponseByString(runner, HttpStatus.OK, "[${id1},${id2},${id3}]");
    }

    @Test(description = "Проверка, что отображается список с одной уточкой",
            priority = 1,
            invocationCount = 2)
    @CitrusTest
    public void getIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        CleanDB(runner);
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "duckId");
        getAllIds(runner);
        validateResponseByString(runner, HttpStatus.OK, "[${duckId}]");
    }

    @Test(description = "Проверка, что отображается пустой список при отсутствии уточек", priority = 1)
    @CitrusTest
    public void getNoIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        CleanDB(runner);
        getAllIds(runner);
        validateResponseByString(runner, HttpStatus.OK, "[]");
    }
}
