package api.adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.Duration;
import java.time.LocalDateTime;

public class GsonUtils {
    public static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }
}