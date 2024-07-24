package org.opensearch.rest.model;

import com.carrotsearch.randomizedtesting.annotations.ThreadLeakScope;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.opensearch.client.Request;
import org.opensearch.client.Response;
import org.opensearch.plugins.Plugin;
import org.opensearch.rest.action.StatisticsPlugin;
import org.opensearch.test.OpenSearchIntegTestCase;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@ThreadLeakScope(ThreadLeakScope.Scope.NONE)
@OpenSearchIntegTestCase.ClusterScope(scope = OpenSearchIntegTestCase.Scope.SUITE)
public class StatisticsTests extends OpenSearchIntegTestCase {

    @Override
    protected Collection<Class<? extends Plugin>> nodePlugins() {
        return Collections.singletonList(StatisticsPlugin.class);
    }

    public void testStatisticsEndpoint() throws IOException, ParseException {
        String testDataJson = "[{\"host\": \"host1\", \"upsAdvBatteryRunTimeRemaining\": 120, \"upsAdvOutputVoltage\": 230}, " +
                "{\"host\": \"host2\", \"upsAdvBatteryRunTimeRemaining\": 150, \"upsAdvOutputVoltage\": 240}]";
        String filePath = createTempFile(testDataJson);

        Request request = new Request("GET", "/_statistics?filePath=" + filePath);
        Response response = getRestClient().performRequest(request);

        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        logger.info("response body: {}", responseBody);

        double expectedAvgRunTime = 135.0;
        int expectedMaxOutputVoltage = 240;
        List<String> expectedHosts = List.of("host1", "host2");

        assertThat(responseBody, containsString(String.valueOf(expectedAvgRunTime)));
        assertThat(responseBody, containsString(String.valueOf(expectedMaxOutputVoltage)));
        assertThat(responseBody, containsString(expectedHosts.get(0)));
        assertThat(responseBody, containsString(expectedHosts.get(1)));
    }

    public void testStatisticsEndpointMissingFilePath() throws IOException, ParseException {
        Request request = new Request("GET", "/_statistics");

        Response response = getRestClient().performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        logger.info("response body: {}", responseBody);

        assertThat(response.getStatusLine().getStatusCode(), equalTo(400)); // Bad Request
        assertThat(responseBody, containsString("Missing 'filePath' parameter"));
    }

    public void testStatisticsEndpointInvalidFile() throws IOException, ParseException {
        Request request = new Request("GET", "/_statistics?filePath=invalid_path");

        Response response = getRestClient().performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        logger.info("response body: {}", responseBody);

        assertThat(response.getStatusLine().getStatusCode(), equalTo(500)); // Internal Server Error
        assertThat(responseBody, containsString("Error reading the JSON file"));
    }

    private String createTempFile(String content) throws IOException {
        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("testdata", ".json");
        java.nio.file.Files.write(tempFile, content.getBytes(StandardCharsets.UTF_8));
        return tempFile.toString();
    }
}
