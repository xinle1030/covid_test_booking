package fit3077.lab02team14.assignment2.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.util.ObjectUtils;

import fit3077.lab02team14.assignment2.model.actor.User;
import fit3077.lab02team14.assignment2.model.modalInfo.UserInfo;

public class NetworkHelper {

    /*
     * NOTE: In order to access the web service, you will need to include your API
     * key in the Authorization header of all requests you make.
     * Your personal API key can be obtained here: https://fit3077.com
     */
    private static final String myApiKey = "kcGfHhWdJNKPBpjJrPFJQHgbDFPDnz";

    // Provide the root URL for the web service. All web service request URLs start
    // with this root URL.
    private static final String rootUrl = "https://fit3077.com/api/v1";

    private static String url;

    public static HttpResponse<String> getData(String urlParams) {
        url = rootUrl + urlParams;
        // This request is authenticated (API key attached in the Authorization header),
        // and will return the JSON array object containing all users.
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(url))
                .setHeader("Authorization", myApiKey)
                .GET()
                .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            if (response.statusCode() != 200) {
                System.out.println(e.getMessage());
            }
        }
        return response;
    }

    public static HttpResponse<String> deleteRequest(String urlParams) {
        url = rootUrl + urlParams;
        // This request is authenticated (API key attached in the Authorization header),
        // and will return the JSON array object containing all users.
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(url))
                .setHeader("Authorization", myApiKey)
                .DELETE()
                .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            if (response.statusCode() != 204) {
                System.out.println(e.getMessage());
            }
        }
        return response;
    }

    public static String getUser(HttpResponse<String> response) throws JsonMappingException, JsonProcessingException {
        if (response != null) {
            // Only stores JWT Token instead of User's username/password
            ObjectNode jsonNode = new ObjectMapper().readValue(response.body(), ObjectNode.class);
            String jwtToken = jsonNode.get("jwt").textValue();

            String decodedJWT = decodeJWT(jwtToken);
            return decodedJWT;
        } else {
            return "";
        }
    }

    public static HttpResponse<String> authUser(String urlParams, UserInfo userInfo) {

        String jsonString = "{" +
                "\"userName\":\"" + userInfo.getUsername() + "\"," +
                "\"password\":\"" + userInfo.getPassword() + "\"" +
                "}";

        url = rootUrl + urlParams;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url + "?jwt=true")) // Return a JWT so we can use it in
                                                                                    // Part 5 later.
                .setHeader("Authorization", myApiKey)
                .header("Content-Type", "application/json") // This header needs to be set when sending a JSON request body.
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            if (response.statusCode() != 200) {
                System.out.println(e.getMessage());
            }
        }
        return response;
    }

    // To decode the JWT Token and retrieve User's information
    public static String decodeJWT(String token) throws JsonProcessingException {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        System.out.println(decoder);

        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        // To correspond the key from JWT and the endpoint
        payload = payload.replace("username", "userName");

        return payload;
    }

    public static HttpResponse<String> verifyJWT(String urlParams, String jsonString) {
        return null;
    }

    public static HttpResponse<String> patchRequest(String urlParams, HashMap<String, Object> inputJsonObj,
            Boolean patchObject) {
        url = rootUrl + urlParams;

        HashMap<String, Object> jsonObj = new HashMap<>();

        if (patchObject) {
            jsonObj = new HashMap<String, Object>() {
                {
                    put("additionalInfo", inputJsonObj);
                }
            };
        } else {
            jsonObj = inputJsonObj;
        }

        String jsonString = toJsonString(jsonObj);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(URI.create(url)) // Return a JWT so we can use it in Part 5 later.
                .setHeader("Authorization", myApiKey)
                .header("Content-Type", "application/json") // This header needs to be set when sending a JSON request
                                                            // body.
                .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            if (response.statusCode() != 200) {
                System.out.println(e.getMessage());
            }
        }
        return response;
    }

    public static HttpResponse<String> patchRequest(String urlParams, HashMap<String, Object> inputJsonObj) {
        return patchRequest(urlParams, inputJsonObj, true);
    }

    public static HttpResponse<String> postRequest(String urlParams, HashMap<String, Object> jsonObj) {
        url = rootUrl + urlParams;
        String jsonString = toJsonString(jsonObj);
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(URI.create(url)) // Return a JWT so we can use it in Part 5 later.
                .setHeader("Authorization", myApiKey)
                .header("Content-Type", "application/json") // This header needs to be set when sending a JSON request
                                                            // body.
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            if (response.statusCode() != 201) {
                System.out.println(e.getMessage());
            }
        }
        return response;
    }

    public static String toJsonString(Object object) {

        String jsonString = "{";

        String template1 = "\"%s\":\"%s\",";
        String template2 = "\"%s\":%s,";

        for (HashMap.Entry<String, Object> set : ((HashMap<String, Object>) object).entrySet()) {
            if (set.getValue() instanceof String) {
                jsonString += String.format(template1, set.getKey(), set.getValue());
            } else {
                if (ObjectUtils.isEmpty(set.getValue())) {
                    jsonString += String.format(template2, set.getKey(), set.getValue());
                } else {
                    jsonString += String.format(template2, set.getKey(), toJsonString(set.getValue()));
                }
            }
        }

        jsonString = jsonString.substring(0, jsonString.length() - 1);
        jsonString += "}";
        return jsonString;
    }

    public static String javaObjToJsonStr(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(obj);
            System.out.println("ResultingJSONstring = " + json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

}
