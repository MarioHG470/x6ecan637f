import com.example.myproject.client.HttpClientUtil;
import com.example.myproject.client.HttpClientWrapper;
import com.example.myproject.client.FakeHttpResponse;  // <-- add this line
import com.example.myproject.exception.ApiException;
import com.example.myproject.model.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class HttpClientUtilParameterizedTest {

    @ParameterizedTest
    @CsvSource({
        "404, Not Found",
        "401, Unauthorized",
        "403, Forbidden",
        "500, Internal Server Error"
    })
    void testErrorHandlingParameterized(int statusCode, String message) throws Exception {
        // Mock wrapper
        HttpClientWrapper mockWrapper = Mockito.mock(HttpClientWrapper.class);
        HttpResponse<String> fakeResponse =
                new FakeHttpResponse(statusCode, "{\"status\":" + statusCode + ",\"message\":\"" + message + "\"}");

        Mockito.when(mockWrapper.send(any(HttpRequest.class))).thenReturn(fakeResponse);

        HttpClientUtil client = new HttpClientUtil(mockWrapper, new ObjectMapper());

        ApiException ex = assertThrows(ApiException.class, () -> {
            client.get("https://jsonplaceholder.typicode.com/test-endpoint", String.class);
        });

        ErrorResponse error = ex.getError();
        assertNotNull(error);
        assertEquals(statusCode, error.getStatus());
        assertEquals(message, error.getMessage());
    }
}
