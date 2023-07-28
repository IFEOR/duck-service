package autotests.clients;

import autotests.BaseTest;
import com.consol.citrus.TestCaseRunner;
import io.qameta.allure.Step;

public class DuckCrudClient extends BaseTest {

    @Step("Эндпоинт создания уточки через json тело")
    public void create(TestCaseRunner runner, String color, String height, String material, String sound, String wingsState) {
        sendPostRequest(runner, "/api/duck/create", "{\n" +
                "  \"color\": \"" + color + "\",\n" +
                "  \"height\": " + height + ",\n" +
                "  \"material\": \"" + material + "\",\n" +
                "  \"sound\": \"" + sound + "\",\n" +
                "  \"wingsState\": \"" + wingsState + "\"\n" +
                "}");
    }

    @Step("Эндпоинт создания уточки через payload")
    public void create(TestCaseRunner runner, Object payload) {
        sendPostRequest(runner, "/api/duck/create", payload);
    }

    @Step("Эндпоинт получения списка id уточек")
    public void getAllIds(TestCaseRunner runner) {
        sendGetRequest(runner, "/api/duck/getAllIds");
    }

    @Step("Эндпоинт изменения характеристик уточки с id = {id}")
    public void update(TestCaseRunner runner, String id, String color, String height, String material, String sound, String wingsState) {
        sendPutRequest(runner, "/api/duck/update?id=" + id +
                "&color=" + color +
                "&height=" + height +
                "&material=" + material +
                "&sound=" + sound +
                "&wingsState=" + wingsState);
    }

    @Step("Эндпоинт удаления уточки с id = {id}")
    public void delete(TestCaseRunner runner, String id) {
        sendDeleteRequest(runner, "/api/duck/delete", "id", id);
    }
}
