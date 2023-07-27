package autotests.clients;

import autotests.BaseTest;
import autotests.payloads.Duck;
import com.consol.citrus.TestAction;
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

    public void create(TestCaseRunner runner, Duck duck) {
        sendPostRequest(runner, "/api/duck/create", duck);
    }

    public void create(TestCaseRunner runner, String resource) {
        sendPostRequest(runner, "/api/duck/create", resource);
    }

    public void delete(TestCaseRunner runner, String id) {
        sendDeleteRequest(runner, "/api/duck/delete", "id", id);
    }
}
