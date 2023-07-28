package autotests.tests.crud;

import autotests.clients.DuckCrudClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

@Epic("Класс тестов для изменения уточки")
public class UpdateTest extends DuckCrudClient {

    @Test(description = "Уточка изменяет свойства", priority = 1)
    @Description("Проверка, что уточка с валидными свойствами обновляет значение на валидное")
    @CitrusTest
    public void updatedToWood(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "duckId");
        update(runner,
                "${duckId}",
                duckDefault.color(),
                String.valueOf(duckDefault.height()),
                "wood",
                duckDefault.sound(),
                duckDefault.wingsState());
        validateResponse(runner, HttpStatus.OK, jsonPath().expression("$.message", "Duck with id = ${duckId} is updated"));
        validateDuckInDatabase(runner, "${duckId}", duckDefault.material("wood"));
    }

    @Flaky
    @Feature("Проверка звука")
    @Test(description = "Уточка не меняет звук на невалидный", priority = 2)
    @Description("Проверка, что уточка с валидными свойствами не обновляет звук на невалидный")
    @CitrusTest
    public void notUpdatedIncorrectSound(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "duckId");
        update(runner,
                "${duckId}",
                duckDefault.color(),
                String.valueOf(duckDefault.height()),
                duckDefault.material(),
                "meow",
                duckDefault.wingsState());
        validateResponse(runner, HttpStatus.BAD_REQUEST, "incorrectSoundMessage.json");
        validateDuckInDatabase(runner, "${duckId}", duckDefault.sound("meow"));
    }

    @Feature("Проверка звука")
    @Test(description = "Уточка может стать немой", priority = 2)
    @Description("Проверка, что уточка с валидными свойствами может стать немой")
    @CitrusTest
    public void updatedEmptySound(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "duckId");
        update(runner,
                "${duckId}",
                duckDefault.color(),
                String.valueOf(duckDefault.height()),
                duckDefault.material(),
                "",
                duckDefault.wingsState());
        validateResponse(runner, HttpStatus.OK, jsonPath().expression("$.message", "Duck with id = ${duckId} is updated"));
        validateDuckInDatabase(runner, "${duckId}", duckDefault.sound(""));
    }

    @Flaky
    @Test(description = "Уточка не может уменьшиться до нулевой высоты", priority = 2)
    @Description("Проверка, что уточка с валидными свойствами не может стать нулевой высоты")
    @CitrusTest
    public void notUpdatedZeroHeight(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "duckId");
        update(runner,
                "${duckId}",
                duckDefault.color(),
                "0.0",
                duckDefault.material(),
                duckDefault.sound(),
                duckDefault.wingsState());
        validateResponse(runner, HttpStatus.BAD_REQUEST, "incorrectHeightMessage.json");
        validateDuckInDatabase(runner, "${duckId}", duckDefault.height(0.0));
    }
}
