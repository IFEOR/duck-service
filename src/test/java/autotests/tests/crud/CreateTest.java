package autotests.tests.crud;

import autotests.clients.DuckCrudClient;
import autotests.payloads.Duck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.CitrusParameters;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Flaky;
import org.springframework.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Класс тестов для создания уточки")
public class CreateTest extends DuckCrudClient {

    @Test(description = "Уточка создаётся",
            priority = 1,
            invocationCount = 2)
    @Description("Проверка, что уточка со всеми валидными свойствами создаётся")
    @CitrusTest
    public void createdDefault(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        create(runner, duckDefault);
        validateResponseWithIdExtraction(runner, HttpStatus.OK, "createDuckTest/createDefault.json");
        validateDuckInDatabase(runner, "${duckId}", duckDefault);
    }

    @Flaky
    @Test(description = "Уточка с нулевой высотой не создаётся", priority = 1)
    @Description("Проверка, что уточка с нулевой высотой не создаётся")
    @CitrusTest
    public void notCreatedZeroHeight(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        create(runner, duckDefault.height(0.0));
        validateResponseWithIdExtraction(runner, HttpStatus.BAD_REQUEST, "incorrectHeightMessage.json");
        validateDuckInDatabase(runner, "${duckId}", duckDefault.height(0.0));
    }

    @Flaky
    @Test(description = "Уточка с невалидным звуком не создаётся", priority = 1)
    @Description("Проверка, что уточка с невалидным звуком не создаётся")
    @CitrusTest
    public void notCreatedInvalidSound(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        create(runner, duckDefault.sound("meow"));
        validateResponseWithIdExtraction(runner, HttpStatus.BAD_REQUEST, "incorrectSoundMessage.json");
        validateDuckInDatabase(runner, "${duckId}", duckDefault.sound("meow"));
    }

    @Test(description = "Уточка создаётся с параметрами по-умолчанию", priority = 1)
    @Description("Проверка, что уточка без параметров создаётся с параметрами по-умолчанию")
    @CitrusTest
    public void createdEmpty(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        create(runner, new Duck());
        validateResponseWithIdExtraction(runner, HttpStatus.OK, "createDuckTest/createEmpty.json");
        validateDuckInDatabase(runner, "${duckId}", new Duck()
                .color("")
                .height(0.0)
                .material("")
                .sound("")
                .wingsState("UNDEFINED")
        );
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

    @Test(description = "Создаётся список разных уточек", dataProvider = "duckSuccessfulList")
    @Description("Проверка, что создаётся список уточек")
    @CitrusTest
    @CitrusParameters({"payload", "runner"})
    public void createdList(Duck duck, @Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        create(runner, duck);
        validateResponseByStringWithIdExtraction(runner, HttpStatus.OK, "{\n" +
                "  \"id\": ${duckId},\n" +
                "  \"color\": \"" + duck.color() + "\",\n" +
                "  \"height\": " + duck.height() + ",\n" +
                "  \"material\": \"" + duck.material() + "\",\n" +
                "  \"sound\": \"" + duck.sound() + "\",\n" +
                "  \"wingsState\": \"" + duck.wingsState() + "\"\n" +
                "}");
        validateDuckInDatabase(runner, "${duckId}", duck);
    }

    @Flaky
    @Test(description = "Валидные уточки создаются, а невалидные - нет", dataProvider = "duckMixedList")
    @Description("Проверка, что часть уточек из списка создаётся, а часть не создаётся")
    @CitrusTest
    @CitrusParameters({"payload", "response", "status", "runner"})
    public void partiallyCreatedList(Object payload, String response, HttpStatus status, @Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        create(runner, payload);
        validateResponseWithIdExtraction(runner, status, response);
        validateDuckInDatabase(runner, "${duckId}", (Duck) payload);
    }
}
