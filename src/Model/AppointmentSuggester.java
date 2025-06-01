package Model;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AppointmentSuggester {
	private TextArea txtSuggest;
	private final HttpClient httpClient = HttpClient.newHttpClient();
	private final ObjectMapper objectMapper = new ObjectMapper();

	public AppointmentSuggester(TextArea txtSuggest) {
		this.txtSuggest = txtSuggest;
	}

	public void createSuggest(String patientId, List<String> serviceIds, int urgency, boolean isFollowup) {
		try {
			// Tạo request body dưới dạng JSON
			Map<String, Object> requestBody = Map.of("patient_id", patientId, "service_ids", serviceIds,
					"urgency_level", urgency, "is_followup", isFollowup);

			String jsonRequest = objectMapper.writeValueAsString(requestBody);

			System.out.println("send");

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8000/suggest_appointments"))
					.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
					.build();

			// Gửi request bất đồng bộ
			httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {
				int statusCode = response.statusCode();
				String responseBody = response.body();
				processResponse(statusCode, responseBody);
			}).exceptionally(e -> {
				e.printStackTrace();
				Platform.runLater(() -> txtSuggest.setText("Error calling API: " + e.getMessage()));
				return null;
			});
		} catch (IOException e) {
			e.printStackTrace();
			Platform.runLater(() -> txtSuggest.setText("Error request JSON: " + e.getMessage()));
		}
	}

	private void processResponse(int statusCode, String jsonResponse) {
        try {
            System.out.println("HTTP Status Code: " + statusCode);
            System.out.println("Response Body: " + jsonResponse);

            List<Map<String, Object>> suggestions = null;

            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            if (rootNode.isArray()) {
                suggestions = objectMapper.readValue(
                        jsonResponse, new TypeReference<>() {});
            } else if (rootNode.isObject()) {
                Map<String, Object> error = objectMapper.readValue(
                        jsonResponse, new TypeReference<>() {});
                String errorMsg = "Lỗi từ server: " + error.getOrDefault("detail", "Not recognized");
                Platform.runLater(() -> txtSuggest.setText(errorMsg));
                return;
            }

            if (suggestions == null || suggestions.isEmpty()) {
                Platform.runLater(() -> txtSuggest.setText("No suggestion."));
                return;
            }

            StringBuilder sb = new StringBuilder();
            int index = 1;
            for (Map<String, Object> suggestion : suggestions) {
                String doctorName  = (String) suggestion.get("doctor_name");
                String serviceName  = (String) suggestion.get("service_name");
                String slotTimeStr = (String) suggestion.get("slot_time");

                LocalDateTime slotDateTime = LocalDateTime.parse(slotTimeStr);

                String formattedSlotTime = slotDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

                sb.append(index++)
                	.append("📌 Service: ").append(serviceName).append("\n")
                  .append("👨‍⚕️ Doctor: ").append(doctorName).append("\n")
                  .append("⏰ Time: ").append(formattedSlotTime)
                  .append("\n\n");
            }

            String text = sb.toString();
            Platform.runLater(() -> txtSuggest.setText(text));

        } catch (IOException e) {
            e.printStackTrace();
            Platform.runLater(() -> txtSuggest.setText("Error while trying to solve response: " + e.getMessage()));
        }
    }
}
