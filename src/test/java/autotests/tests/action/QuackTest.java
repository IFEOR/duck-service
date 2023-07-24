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
        try {
            create(runner, duckDefault);
            extractIdFromResponse(runner);
            quack(runner, "${id}", "1", "1");
            validateResponse(runner, HttpStatus.OK, "quackDuckTest/quackSingle.json");
        } finally {
            delete(runner, "${id}");
        }
    }

    @Test(description = "Проверка, что уточка с валидным id (существующая в БД уточка) крякает 5 раз одним кряком", priority = 1)
    @CitrusTest
    public void quackFewRepetitions(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            create(runner, duckDefault);
            extractIdFromResponse(runner);
            quack(runner, "${id}", "5", "1");
            validateResponse(runner, HttpStatus.OK, "quackDuckTest/quackFewRepetitions.json");
        } finally {
            delete(runner, "${id}");
        }
    }

    @Test(description = "Проверка, что уточка с валидным id (существующая в БД уточка) крякает 1 раз пятью кряками", priority = 1)
    @CitrusTest
    public void quackFewSounds(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            create(runner, duckDefault);
            extractIdFromResponse(runner);
            quack(runner, "${id}", "1", "5");
            validateResponse(runner, HttpStatus.OK, "quackDuckTest/quackFewSounds.json");
        } finally {
            delete(runner, "${id}");
        }
    }

    @Test(description = "Проверка, что немая уточка с валидным id (существующая в БД уточка) не крякает", priority = 2)
    @CitrusTest
    public void quackMuteDuck(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            create(runner, duckDefault.sound(""));
            extractIdFromResponse(runner);
            quack(runner, "${id}", "1", "1");
            validateResponse(runner, HttpStatus.OK, "quackDuckTest/quackEmpty.json");
        } finally {
            delete(runner, "${id}");
        }
    }
}
