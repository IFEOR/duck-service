package autotests.tests.crud;

import autotests.clients.DuckCrudClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DeleteTest extends DuckCrudClient {

    @Test(description = "Проверка, что уточка с валидным id (существующая в БД уточка) удаляется",
            priority = 1,
            invocationCount = 2)
    @CitrusTest
    public void deleteExist(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        create(runner, "yellow", "5.00", "rubber", "quack", "ACTIVE");
        extractIdFromResponse(runner);
        delete(runner, "${id}");
        validateResponse(runner, HttpStatus.OK, "deleteDuckTest/deleteExist.json");
    }
}
