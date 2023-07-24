package autotests.tests.crud;

import autotests.clients.DuckCrudClient;
import autotests.payloads.Duck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class CreateTest extends DuckCrudClient {

    @Test(description = "Проверка, что уточка со всеми валидными свойствами создаётся",
            priority = 1,
            invocationCount = 2)
    @CitrusTest
    public void createdDefault(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            create(runner, duckDefault);
            validateResponseWithIdExtraction(runner, HttpStatus.OK, "createDuckTest/createDefault.json");
        } finally {
            delete(runner, "${id}");
        }
    }

    @Test(description = "Проверка, что уточка с нулевой высотой не создаётся", priority = 1)
    @CitrusTest
    public void notCreatedZeroHeight(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            create(runner, duckDefault.height(0.0));
            validateResponseWithIdExtraction(runner, HttpStatus.BAD_REQUEST, "incorrectHeightMessage.json");
        } finally {
            delete(runner, "${id}");
        }
    }

    @Test(description = "Проверка, что уточка с невалидным звуком не создаётся", priority = 1)
    @CitrusTest
    public void notCreatedInvalidSound(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            create(runner, duckDefault.sound("meow"));
            validateResponseWithIdExtraction(runner, HttpStatus.BAD_REQUEST, "incorrectSoundMessage.json");
        } finally {
            delete(runner, "${id}");
        }
    }

    @Test(description = "Проверка, что уточка без параметров создаётся с параметрами по-умолчанию", priority = 1)
    @CitrusTest
    public void createdEmpty(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            create(runner, new Duck());
            validateResponseWithIdExtraction(runner, HttpStatus.OK, "createDuckTest/createEmpty.json");
        } finally {
            delete(runner, "${id}");
        }
    }
}
