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
        try {
            create(runner, duckDefault);
            extractIdFromResponse(runner);
            properties(runner, "${id}");
            validateResponse(runner, HttpStatus.OK, duckDefault);
        } finally {
            delete(runner, "${id}");
        }
    }

    @Test(description = "Проверка, что деревянная уточка с валидным id (существующая в БД уточка) передаёт верную информацию о себе",
            priority = 1)
    @CitrusTest
    public void propertiesWoodMaterial(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            create(runner, duckWood);
            extractIdFromResponse(runner);
            properties(runner, "${id}");
            validateResponse(runner, HttpStatus.OK, duckWood);
        } finally {
            delete(runner, "${id}");
        }
    }
}
