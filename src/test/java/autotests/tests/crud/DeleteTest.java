package autotests.tests.crud;

import autotests.clients.DuckCrudClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Класс тестов для удаления уточки")
public class DeleteTest extends DuckCrudClient {

    @Test(description = "Уточка удаляется",
            priority = 1,
            invocationCount = 2)
    @Description("Проверка, что уточка с валидным id (существующая в БД уточка) удаляется")
    @Owner(value = "Китова А.И.")
    @CitrusTest
    public void deleteExist(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "duckId");
        delete(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, "deleteDuckTest/deleteExist.json");
    }
}
