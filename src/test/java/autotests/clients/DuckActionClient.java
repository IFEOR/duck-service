package autotests.clients;

import autotests.BaseTest;
import com.consol.citrus.TestCaseRunner;

public class DuckActionClient extends BaseTest {

    public void fly(TestCaseRunner runner, String id) {
        sendGetRequest(runner, "/api/duck/action/fly", "id", id);
    }

    public void properties(TestCaseRunner runner, String id) {
        sendGetRequest(runner, "/api/duck/action/properties", "id", id);
    }

    public void quack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        sendGetRequest(runner,
                "/api/duck/action/quack?id=" + id + "&repetitionCount=" + repetitionCount + "&soundCount=" + soundCount);
    }

    public void swim(TestCaseRunner runner, String id) {
        sendGetRequest(runner, "/api/duck/action/swim", "id", id);
    }
}
