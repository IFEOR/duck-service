package autotests.tests.action;

import autotests.clients.DuckActionClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.*;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Класс тестов для получения характеристик уточки")
public class PropertiesTest extends DuckActionClient {

    @Flaky
    @Test(description = "Резиновая уточка передаёт свойства",
            priority = 1)
    @Description("Проверка, что стандартная уточка с валидным id (существующая в БД уточка) передаёт верную информацию о себе")
    @Severity(value = SeverityLevel.MINOR)
    @CitrusTest
    public void propertiesDefault(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "duckId");
        properties(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, duckDefault);
    }

    @Flaky
    @Test(description = "Деревянная уточка передаёт свойства",
            priority = 1)
    @Description("Проверка, что деревянная уточка с валидным id (существующая в БД уточка) передаёт верную информацию о себе")
    @Severity(value = SeverityLevel.MINOR)
    @CitrusTest
    public void propertiesWoodMaterial(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckWood);
        extractLastCreatedDuckId(runner, "duckId");
        properties(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, duckWood);
    }
}
