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
        try {
            create(runner, duckDefault);
            extractIdFromResponse(runner);
            update(runner,
                    "${id}",
                    duckDefault.color(),
                    String.valueOf(duckDefault.height()),
                    "wood",
                    duckDefault.sound(),
                    duckDefault.wingsState());
            validateResponse(runner, HttpStatus.OK, jsonPath().expression("$.message", "Duck with id = ${id} is updated"));
        } finally {
            delete(runner, "${id}");
        }
    }

    @Test(description = "Проверка, что уточка с валидными свойствами не обновляет звук на невалидный", priority = 2)
    @CitrusTest
    public void notUpdatedIncorrectSound(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            create(runner, duckDefault);
            extractIdFromResponse(runner);
            update(runner,
                    "${id}",
                    duckDefault.color(),
                    String.valueOf(duckDefault.height()),
                    duckDefault.material(),
                    "meow",
                    duckDefault.wingsState());
            validateResponse(runner, HttpStatus.BAD_REQUEST, "incorrectSoundMessage.json");
        } finally {
            delete(runner, "${id}");
        }
    }

    @Test(description = "Проверка, что уточка с валидными свойствами может стать немой", priority = 2)
    @CitrusTest
    public void updatedEmptySound(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            create(runner, duckDefault);
            extractIdFromResponse(runner);
            update(runner,
                    "${id}",
                    duckDefault.color(),
                    String.valueOf(duckDefault.height()),
                    duckDefault.material(),
                    "",
                    duckDefault.wingsState());
            validateResponse(runner, HttpStatus.OK, jsonPath().expression("$.message", "Duck with id = ${id} is updated"));
        } finally {
            delete(runner, "${id}");
        }
    }

    @Test(description = "Проверка, что уточка с валидными не может стать нулевой высоты", priority = 2)
    @CitrusTest
    public void notUpdatedZeroHeight(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            create(runner, duckDefault);
            extractIdFromResponse(runner);
            update(runner,
                    "${id}",
                    duckDefault.color(),
                    String.valueOf(duckDefault.height()),
                    duckDefault.material(),
                    "",
                    duckDefault.wingsState());
            validateResponse(runner, HttpStatus.BAD_REQUEST, "incorrectHeightMessage.json");
        } finally {
            delete(runner, "${id}");
        }
    }
}
