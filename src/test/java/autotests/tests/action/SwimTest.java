package autotests.tests.action;

import autotests.clients.DuckActionClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class SwimTest extends DuckActionClient {

    @Test(description = "Проверка, что уточка с валидным id (существующая в БД уточка) плывёт", priority = 1)
    @CitrusTest
    public void swimValidId(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "duckId");
        swim(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, "swimDuckTest/swimSuccess.json");
    }
}
