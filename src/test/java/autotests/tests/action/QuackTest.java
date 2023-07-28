package autotests.tests.action;

import autotests.clients.DuckActionClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.*;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Класс тестов для кряканья уточки")
public class QuackTest extends DuckActionClient {

    @Flaky
    @Test(description = "Уточка крякает 1 раз",
            priority = 1,
            invocationCount = 2)
    @Description("Проверка, что уточка с валидным id (существующая в БД уточка) крякает 1 раз одним кряком")
    @Owner(value = "Китова А.И.")
    @Severity(value = SeverityLevel.NORMAL)
    @CitrusTest
    public void quackDefault(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "duckId");
        quack(runner, "${duckId}", "1", "1");
        validateResponse(runner, HttpStatus.OK, "quackDuckTest/quackSingle.json");
    }

    @Flaky
    @Test(description = "Уточка крякает 5 раз одним кряком", priority = 1)
    @Description("Проверка, что уточка с валидным id (существующая в БД уточка) крякает 5 раз одним кряком")
    @Owner(value = "Китова А.И.")
    @Severity(value = SeverityLevel.MINOR)
    @CitrusTest
    public void quackFewRepetitions(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "duckId");
        quack(runner, "${duckId}", "5", "1");
        validateResponse(runner, HttpStatus.OK, "quackDuckTest/quackFewRepetitions.json");
    }

    @Flaky
    @Test(description = "Уточка крякает 1 раз пятью кряками", priority = 1)
    @Description("Проверка, что уточка с валидным id (существующая в БД уточка) крякает 1 раз пятью кряками")
    @Owner(value = "Китова А.И.")
    @Severity(value = SeverityLevel.MINOR)
    @CitrusTest
    public void quackFewSounds(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault);
        extractLastCreatedDuckId(runner, "duckId");
        quack(runner, "${duckId}", "1", "5");
        validateResponse(runner, HttpStatus.OK, "quackDuckTest/quackFewSounds.json");
    }

    @Test(description = "Немая уточка не крякает", priority = 2)
    @Description("Проверка, что немая уточка с валидным id (существующая в БД уточка) не крякает")
    @Owner(value = "Китова А.И.")
    @Severity(value = SeverityLevel.NORMAL)
    @CitrusTest
    public void quackMuteDuck(@Optional @CitrusResource TestCaseRunner runner) {
        finallyDuckDelete(runner);
        createTestDuck(runner, duckDefault.sound(""));
        extractLastCreatedDuckId(runner, "duckId");
        quack(runner, "${duckId}", "1", "1");
        validateResponse(runner, HttpStatus.OK, "quackDuckTest/quackEmpty.json");
    }
}
