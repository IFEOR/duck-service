package autotests.tests.crud;

import autotests.clients.DuckCrudClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class UpdateTest extends DuckCrudClient {

    @Test(description = "Проверка, что уточка с валидными свойствами обновляет значение на валидное", priority = 1)
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

    @Test(description = "Проверка, что уточка с валидными свойствами не обновляет звук на невалидный", priority = 2)
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

    @Test(description = "Проверка, что уточка с валидными свойствами может стать немой", priority = 2)
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

    @Test(description = "Проверка, что уточка с валидными свойствами не может стать нулевой высоты", priority = 2)
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
