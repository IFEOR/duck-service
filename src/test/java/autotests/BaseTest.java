package autotests;

import autotests.payloads.Duck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.exceptions.TestCaseFailedException;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.consol.citrus.validation.json.JsonPathMessageValidationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.consol.citrus.DefaultTestActionBuilder.action;
import static com.consol.citrus.actions.EchoAction.Builder.echo;
import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.DelegatingPayloadVariableExtractor.Builder.fromBody;
import static java.util.Map.entry;

@ContextConfiguration(classes = {EndpointConfig.class})
public class BaseTest extends TestNGCitrusSpringSupport {

    @Autowired
    protected HttpClient duckService;

    @Autowired
    protected SingleConnectionDataSource testDatabase;

    protected AtomicInteger uniqueId = new AtomicInteger(0);

    protected Duck duckDefault = new Duck().color("yellow").height(5.0).material("rubber").sound("quack").wingsState("ACTIVE");
    protected Duck duckWood = new Duck().color("yellow").height(5.0).material("wood").sound("quack").wingsState("ACTIVE");

    protected void sendGetRequest(TestCaseRunner runner, String path) {
        runner.$(http()
                .client(duckService)
                .send()
                .get(path)
        );
    }

    protected void sendGetRequest(TestCaseRunner runner, String path, String name, String value) {
        runner.$(http()
                .client(duckService)
                .send()
                .get(path)
                .queryParam(name, value)
        );
    }

    protected void sendPostRequest(TestCaseRunner runner, String path, Object payload) {
        runner.$(http()
                .client(duckService)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(payload, new ObjectMapper())));
    }

    protected void sendPostRequest(TestCaseRunner runner, String path, String body) {
        runner.$(http()
                .client(duckService)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ClassPathResource(body)));
    }

    protected void sendPutRequest(TestCaseRunner runner, String path) {
        runner.$(http().client(duckService)
                .send()
                .put(path)
        );
    }

    protected void sendPutRequest(TestCaseRunner runner, String path, Duck duck) {
        runner.$(http().client(duckService)
                .send()
                .put(path)
                .queryParam("id", String.valueOf(duck.id()))
                .queryParam("color", duck.color())
                .queryParam("height", String.valueOf(duck.height()))
                .queryParam("material", duck.material())
                .queryParam("sound", duck.sound())
                .queryParam("wingsState", duck.wingsState())
        );
    }

    protected void sendDeleteRequest(TestCaseRunner runner, String path, String name, String value) {
        runner.$(http().client(duckService)
                .send()
                .delete(path)
                .queryParam(name, value)
        );
    }

    //region Валидация ответов

    @Description("Валидация полученного ответа String'ой")
    protected void validateResponseByString(TestCaseRunner runner, HttpStatus status, String body) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(status)
                .message()
                .type(MessageType.JSON)
                .body(body)
        );
    }

    @Description("Валидация полученного ответа String'ой")
    protected void validateResponseByStringWithIdExtraction(TestCaseRunner runner, HttpStatus status, String body) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(status)
                .message()
                .type(MessageType.JSON)
                .extract(fromBody().expression("$.id", "duckId"))
                .body(body)
        );
    }

    @Description("Валидация полученного ответа из папки Resources")
    protected void validateResponse(TestCaseRunner runner, HttpStatus status, String expectedPayload) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(status)
                .message()
                .type(MessageType.JSON)
                .body(new ClassPathResource(expectedPayload))
        );
    }

    protected void validateResponseWithIdExtraction(TestCaseRunner runner, HttpStatus status, String expectedPayload) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(status)
                .message()
                .type(MessageType.JSON)
                .extract(fromBody().expression("$.id", "duckId"))
                .body(new ClassPathResource(expectedPayload))
        );
    }

    @Description("Валидация полученного ответа из папки Payload")
    protected void validateResponse(TestCaseRunner runner, HttpStatus status, Object expectedPayload) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(status)
                .message()
                .type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper()))
        );
    }

    @Description("Валидация полученного ответа с помощью JsonPath")
    protected void validateResponse(TestCaseRunner runner, HttpStatus status, JsonPathMessageValidationContext.Builder body) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(status)
                .message()
                .type(MessageType.JSON)
                .validate(body)
        );
    }

    //endregion

    //region Экстракторы

    protected void extractIdFromResponse(TestCaseRunner runner) {
        extractDataFromResponse(runner, "$.id", "duckId");
    }

    protected void extractDuckPropertiesFromResponse(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .extract(fromBody().expressions(Map.ofEntries(
                                entry("$.color", "color"),
                                entry("$.height", "height"),
                                entry("$.material", "material"),
                                entry("$.sound", "sound"),
                                entry("$.wingsState", "wingsState")
                        ))
                )
        );
    }

    protected void extractDataFromResponse(TestCaseRunner runner, String path, String name) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .extract(fromBody().expression(path, name))
        );
    }

    //endregion

    protected void getId(TestCaseRunner runner) {
        try {
            runner.$(query(testDatabase)
                    .statement("SELECT * FROM DUCK ORDER BY id DESC")
                    .extract("id", "maxId")
            );
            runner.$(action(context -> {
                uniqueId.set(Integer.parseInt(context.getVariable("${maxId}")));
            }));
        } catch (TestCaseFailedException e) {
            runner.$(echo("Database is empty. No need to define id. Indexing will start from 1"));
        }
    }

    protected void createTestDuck(TestCaseRunner runner, Duck duck) {
        if (uniqueId.get() == 0) getId(runner);
        runner.$(sql(testDatabase)
                .statement("INSERT INTO DUCK (id, color, height, material, sound, wings_State) \n" +
                        "\tVALUES (" +
                        uniqueId.addAndGet(1) + ", '" +
                        duck.color() + "', " +
                        duck.height() + ", '" +
                        duck.material() + "', '" +
                        duck.sound() + "', '" +
                        duck.wingsState() + "');\n")
        );
    }

    protected void extractLastCreatedDuckId(TestCaseRunner runner, String variableName) {
        runner.$(query(testDatabase)
                .statement("SELECT id FROM duck WHERE id = " + uniqueId.get())
                .extract("id", variableName)
        );
    }


    protected void finallyDuckDelete(TestCaseRunner runner) {
        runner.variable("${duckId}", "0");
        runner.$(doFinally().actions(runner.$(sql(testDatabase)
                .statement("DELETE FROM DUCK WHERE id = ${duckId}")))
        );
    }

    protected void finallyDuckDelete(TestCaseRunner runner, String variableName) {
        runner.variable(variableName, "0");
        runner.$(doFinally().actions(runner.$(sql(testDatabase)
                .statement("DELETE FROM duck WHERE id = " + variableName)))
        );
    }

    protected void CleanDB(TestCaseRunner runner) {
        runner.$(sql(testDatabase).statement("DELETE FROM DUCK"));
    }

    protected void validateDuckInDatabase(TestCaseRunner runner, String id, Duck duck) {
        runner.$(query(testDatabase)
                .statement("SELECT * FROM duck WHERE id = " + id)
                .validate("COLOR", duck.color())
                .validate("HEIGHT", String.valueOf(duck.height()))
                .validate("MATERIAL", duck.material())
                .validate("SOUND", duck.sound())
                .validate("WINGS_STATE", duck.wingsState())
        );
    }
}
