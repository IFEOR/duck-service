package autotests.tests.action;

import autotests.clients.DuckActionClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.*;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Класс тестов для плавания уточки")
public class SwimTest extends DuckActionClient {

    @Flaky
    @Test(description = "Уточка плывёт", priority = 1)
    @Description("Проверка, что уточка с валидным id (существующая в БД уточка) плывёт")
    @Severity(value = SeverityLevel.NORMAL)
    @CitrusTest
    public void swimValidId(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "duckId");
        swim(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, "swimDuckTest/swimSuccess.json");
    }
}
