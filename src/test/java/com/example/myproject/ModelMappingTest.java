package com.example.myproject.model;

import com.example.myproject.model.User;
import com.example.myproject.model.Address;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ModelMappingTest {

    @Test
    void testUserJsonMapping() throws Exception {
        String json = """
        {
          "id": 1,
          "name": "Mario",
          "email": "marioo@example.com",
          "address": {
            "street": "Main St",
            "city": "Chilpancingo",
            "zipcode": "39000"
          }
        }
        """;

        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(json, User.class);

        assertEquals(1, user.getId());
        assertEquals("Mario", user.getName());
        assertEquals("Chilpancingo", user.getAddress().getCity());
    }
}
