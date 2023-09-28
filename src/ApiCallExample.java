// Using okhttp to make http requests to the Edamam API.
import okhttp3.*;

// Using gson to parse JSON.
import com.google.gson.*;

import java.io.IOException;

public class ApiCallExample {
    private static final String APP_ID = System.getenv("APP_ID");
    private static final String API_KEY = System.getenv("API_KEY");

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        // Making a GET request to retrieve a list of foods that are between 100 and 300 kcals per serving.
        Request req = new Request.Builder()
                .url(String.format(
                        "https://api.edamam.com/api/food-database/v2/parser?app_id=%s&app_key=%s&calories=100-300",
                        APP_ID,
                        API_KEY))
                .build();

        try {
            // Executing GET request
            Response res = client.newCall(req).execute();

            // Printing out res object
            System.out.println(res);

            // Making sure that the request was successful and response body is not null
            if (res.code() == 200 && res.body() != null) {
                // Parsing JSON string
                JsonObject resBody = JsonParser.parseString(res.body().string()).getAsJsonObject();

                // Converting "hints" to JsonArray
                JsonArray foods = resBody.get("hints").getAsJsonArray();

                // Iterating through foods and printing out each food item's "label"
                for (JsonElement foodItem : foods) {
                    JsonObject foodObject = foodItem.getAsJsonObject().get("food").getAsJsonObject();
                    System.out.println(foodObject.get("label"));
                }

            } else {
                throw new IOException("Status: " + res.code());
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
