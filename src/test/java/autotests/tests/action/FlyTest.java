package autotests.tests.action;

import autotests.clients.DuckActionClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class FlyTest extends DuckActionClient {

    @Test(description = "Проверка, что уточка с валидным id (существующая в БД уточка) и несвязанными крыльями летит", priority = 1)
    @CitrusTest
    public void flyActiveWings(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "duckId");
        fly(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, "flyDuckTest/flySuccess.json");
    }

    @Test(description = "Проверка, что уточка с валидным id (существующая в БД уточка) и связанными крыльями не летит", priority = 1)
    @CitrusTest
    public void flyFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault.wingsState("FIXED"));
        extractLastCreatedDuckId(runner, "duckId");
        fly(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, "flyDuckTest/flyUnsuccess.json");
    }

    @Test(description = "Проверка, что уточка с валидным id (существующая в БД уточка) и отсутствующими крыльями не летит", priority = 1)
    @CitrusTest
    public void flyUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault.wingsState("UNDEFINED"));
        extractLastCreatedDuckId(runner, "duckId");
        fly(runner, "${duckId}");
        validateResponse(runner, HttpStatus.NOT_FOUND, jsonPath().expression("$.message", "Wings aren't detected"));
    }
}
