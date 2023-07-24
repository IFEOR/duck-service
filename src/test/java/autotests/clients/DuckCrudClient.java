package autotests.clients;

import autotests.BaseTest;
import autotests.payloads.Duck;
import com.consol.citrus.TestCaseRunner;

public class DuckCrudClient extends BaseTest {

    public void create(TestCaseRunner runner, String color, String height, String material, String sound, String wingsState) {
        sendPostRequest(runner, "/api/duck/create", "{\n" +
                "  \"color\": \"" + color + "\",\n" +
                "  \"height\": " + height + ",\n" +
                "  \"material\": \"" + material + "\",\n" +
                "  \"sound\": \"" + sound + "\",\n" +
                "  \"wingsState\": \"" + wingsState + "\"\n" +
                "}");
    }

    public void create(TestCaseRunner runner, Duck duck) {
        sendPostRequest(runner, "/api/duck/create", duck);
    }

    public void getAllIds(TestCaseRunner runner) {
        sendGetRequest(runner, "/api/duck/getAllIds");
    }

    public void update(TestCaseRunner runner, String id, String color, String height, String material, String sound, String wingsState) {
        sendPutRequest(runner, "/api/duck/update?id=" + id +
                "&color=" + color +
                "&height=" + height +
                "&material=" + material +
                "&sound=" + sound +
                "&wingsState=" + wingsState);
    }

    public void delete(TestCaseRunner runner, String id) {
        sendDeleteRequest(runner, "/api/duck/delete", "id", id);
    }
}
