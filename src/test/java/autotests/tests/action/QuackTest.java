package autotests.tests.action;

import autotests.clients.DuckActionClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class QuackTest extends DuckActionClient {

    @Test(description = "Проверка, что уточка с валидным id (существующая в БД уточка) крякает 1 раз одним кряком",
            priority = 1,
            invocationCount = 2)
    @CitrusTest
    public void quackDefault(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "duckId");
        quack(runner, "${duckId}", "1", "1");
        validateResponse(runner, HttpStatus.OK, "quackDuckTest/quackSingle.json");
    }

    @Test(description = "Проверка, что уточка с валидным id (существующая в БД уточка) крякает 5 раз одним кряком", priority = 1)
    @CitrusTest
    public void quackFewRepetitions(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "duckId");
        quack(runner, "${duckId}", "5", "1");
        validateResponse(runner, HttpStatus.OK, "quackDuckTest/quackFewRepetitions.json");
    }

    @Test(description = "Проверка, что уточка с валидным id (существующая в БД уточка) крякает 1 раз пятью кряками", priority = 1)
    @CitrusTest
    public void quackFewSounds(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "duckId");
        quack(runner, "${duckId}", "1", "5");
        validateResponse(runner, HttpStatus.OK, "quackDuckTest/quackFewSounds.json");
    }

    @Test(description = "Проверка, что немая уточка с валидным id (существующая в БД уточка) не крякает", priority = 2)
    @CitrusTest
    public void quackMuteDuck(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault.sound(""));
        extractLastCreatedDuckId(runner, "duckId");
        quack(runner, "${duckId}", "1", "1");
        validateResponse(runner, HttpStatus.OK, "quackDuckTest/quackEmpty.json");
    }
}
