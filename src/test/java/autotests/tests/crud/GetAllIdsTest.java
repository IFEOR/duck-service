package autotests.tests.crud;

import autotests.clients.DuckCrudClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.consol.citrus.DefaultTestActionBuilder.action;

public class GetAllIdsTest extends DuckCrudClient {

    private void CleanDB(TestCaseRunner runner) {
        getAllIds(runner);
        extractDataFromResponse(runner, "$", "data");
        runner.$(action(context -> {
            List<String> idList = new ArrayList<>(Arrays.asList(context.getVariable("${data}")
                    .replace("[", "")
                    .replace("]", "")
                    .split(",")
            ));
            for (String id : idList) {
                delete(runner, id);
            }
        }));
    }

    @Test(description = "Проверка, что отображается список нескольких уточек", priority = 1)
    @CitrusTest
    public void getFewIdsTest(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            CleanDB(runner);
            create(runner, duckDefault);
            extractDataFromResponse(runner, "$.id", "id1");
            create(runner, duckDefault);
            extractDataFromResponse(runner, "$.id", "id2");
            create(runner, duckDefault);
            extractDataFromResponse(runner, "$.id", "id3");
            getAllIds(runner);
            validateResponseByString(runner, HttpStatus.OK, "[${id1},${id2},${id3}]");
        } finally {
            delete(runner, "${id1}");
            delete(runner, "${id2}");
            delete(runner, "${id3}");
        }
    }

    @Test(description = "Проверка, что отображается список с одной уточкой",
            priority = 1,
            invocationCount = 2)
    @CitrusTest
    public void getIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            CleanDB(runner);
            create(runner, duckDefault);
            extractIdFromResponse(runner);
            getAllIds(runner);
            validateResponseByString(runner, HttpStatus.OK, "[${id}]");
        } finally {
            delete(runner, "${id}");
        }
    }

    @Test(description = "Проверка, что отображается пустой список при отсутствии уточек", priority = 1)
    @CitrusTest
    public void getNoIdTest(@Optional @CitrusResource TestCaseRunner runner) {
        CleanDB(runner);
        getAllIds(runner);
        validateResponseByString(runner, HttpStatus.OK, "[]");
    }
}
