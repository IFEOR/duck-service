package autotests.clients;

import autotests.BaseTest;
import com.consol.citrus.TestCaseRunner;
import io.qameta.allure.Step;

public class DuckActionClient extends BaseTest {

    @Step("Эндпоинт, который заставляет уточку с id = {id} лететь")
    public void fly(TestCaseRunner runner, String id) {
        sendGetRequest(runner, "/api/duck/action/fly", "id", id);
    }

    @Step("Эндпоинт, который заставляет уточку с id = {id} показать свои характеристики")
    public void properties(TestCaseRunner runner, String id) {
        sendGetRequest(runner, "/api/duck/action/properties", "id", id);
    }

    @Step("Эндпоинт, который заставляет уточку с id = {id} крякать (издавать звуки)")
    public void quack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        sendGetRequest(runner,
                "/api/duck/action/quack?id=" + id + "&repetitionCount=" + repetitionCount + "&soundCount=" + soundCount);
    }

    @Step("Эндпоинт, который заставляет уточку с id = {id} плыть")
    public void swim(TestCaseRunner runner, String id) {
        sendGetRequest(runner, "/api/duck/action/swim", "id", id);
    }
}
