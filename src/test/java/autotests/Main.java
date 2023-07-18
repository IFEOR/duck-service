package autotests;

import autotests.payloads.Category;
import autotests.payloads.Order;
import autotests.payloads.Pet;
import autotests.payloads.Tag;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        //Создаем объекты
        Order order = new Order()
                .id(1)
                .petId(3)
                .quantity(10)
                .shipDate("2023-07-18T12:45:20.487Z").
                status("placed")
                .complete(false);

        Pet pet = new Pet()
                .id(1)
                .category(new Category().id(1).name("2"))
                .name("doggie")
                .photoUrls(List.of("some url"))
                .tags(Collections.singletonList(new Tag().id(1).name("tag name")))
                .status("sold");

        //Передаём объекты в метод преобразования объекта класса в Json
        convertAndPrintToJson(order);
        convertAndPrintToJson(pet);
    }

    //метод для преобразования объекта класса в Json
    public static void convertAndPrintToJson(Object jsonBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
            String json = objectWriter.writeValueAsString(jsonBody);

            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
