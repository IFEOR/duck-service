package autotests.tests.action;

import autotests.clients.DuckActionClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class PropertiesTest extends DuckActionClient {

    @Test(description = "Проверка, что стандартная уточка с валидным id (существующая в БД уточка) передаёт верную информацию о себе",
            priority = 1)
    @CitrusTest
    public void propertiesDefault(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "duckId");
        properties(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, duckDefault);
    }

    @Test(description = "Проверка, что деревянная уточка с валидным id (существующая в БД уточка) передаёт верную информацию о себе",
            priority = 1)
    @CitrusTest
    public void propertiesWoodMaterial(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckWood);
        extractLastCreatedDuckId(runner, "duckId");
        properties(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, duckWood);
    }
}
