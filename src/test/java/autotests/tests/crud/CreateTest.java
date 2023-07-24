package autotests.tests.crud;

import autotests.clients.DuckCrudClient;
import autotests.payloads.Duck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.CitrusParameters;
import org.springframework.http.HttpStatus;
import org.testng.annotations.DataProvider;
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

    @DataProvider(name = "duckSuccessfulList")
    public Object[][] DuckSuccessfulListProvider() {
        return new Object[][]{
                {new Duck().color("yellow").height(5.00).material("rubber").sound("quack").wingsState("ACTIVE"), null},
                {new Duck().color("сине-зелёная").height(1.37).material("wood").sound("").wingsState("FIXED"), null},
                {new Duck().color("#DEDEDE").height(0.00001).material("сталь").sound("quack").wingsState("UNDEFINED"), null},
                {new Duck().color("RED").height(99.99).material("man-made fibres").sound("").wingsState("ACTIVE"), null},
                {new Duck().color("").height(1.66666).material("Уран-235").sound("quack").wingsState("FIXED"), null}
        };
    }

    @DataProvider(name = "duckMixedList")
    public Object[][] DuckMixedListProvider() {
        return new Object[][]{
                {new Duck().color("yellow").height(0.0).material("rubber").sound("quack").wingsState("ACTIVE"), "incorrectHeightMessage.json", HttpStatus.BAD_REQUEST, null},
                {new Duck().color("yellow").height(5.0).material("rubber").sound("meow").wingsState("ACTIVE"), "incorrectSoundMessage.json", HttpStatus.BAD_REQUEST, null},
                {new Duck().color("yellow").height(5.0).material("rubber").sound("quack").wingsState("N/D"), "incorrectWingsStateMessage.json", HttpStatus.BAD_REQUEST, null},
                {new Duck().color("yellow").height(5.0).material("rubber").sound("quack").wingsState("ACTIVE"), "createDuckTest/createDefault.json", HttpStatus.OK, null}
        };
    }

    @Test(description = "Проверка, что создаётся список уточек", dataProvider = "duckSuccessfulList")
    @CitrusTest
    @CitrusParameters({"payload", "runner"})
    public void createdList(Duck duck, @Optional @CitrusResource TestCaseRunner runner) {
        try {
            create(runner, duck);
            validateResponseByStringWithIdExtraction(runner, HttpStatus.OK, "{\n" +
                    "  \"id\": ${id},\n" +
                    "  \"color\": \"" + duck.color() + "\",\n" +
                    "  \"height\": " + duck.height() + ",\n" +
                    "  \"material\": \"" + duck.material() + "\",\n" +
                    "  \"sound\": \"" + duck.sound() + "\",\n" +
                    "  \"wingsState\": \"" + duck.wingsState() + "\"\n" +
                    "}");
        } finally {
            delete(runner, "${id}");
        }
    }

    @Test(description = "Проверка, что часть уточек из списка создаётся, а часть не создаётся", dataProvider = "duckMixedList")
    @CitrusTest
    @CitrusParameters({"payload", "response", "status", "runner"})
    public void partiallyCreatedList(Object payload, String response, HttpStatus status, @Optional @CitrusResource TestCaseRunner runner) {
        try {
            create(runner, payload);
            validateResponseWithIdExtraction(runner, status, response);
        } finally {
            delete(runner, "${id}");
        }
    }
}
