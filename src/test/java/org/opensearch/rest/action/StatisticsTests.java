package org.opensearch.rest.action;

import com.carrotsearch.randomizedtesting.annotations.ThreadLeakScope;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.opensearch.client.Request;
import org.opensearch.client.Response;
import org.opensearch.plugins.Plugin;
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
        String index = "test-index";
        String testDataJson = "[{\"host\": \"host1\", \"upsAdvBatteryRunTimeRemaining\": 120, \"upsAdvOutputVoltage\": 230}, " +
                "{\"host\": \"host2\", \"upsAdvBatteryRunTimeRemaining\": 150, \"upsAdvOutputVoltage\": 240}]";
        indexTestData(index, testDataJson);

        Request request = new Request("GET", "/_plugins/statistics/" + index);
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

    public void testStatisticsEndpointMissingIndex() throws IOException, ParseException {
        Request request = new Request("GET", "/_plugins/statistics");

        Response response = getRestClient().performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        logger.info("response body: {}", responseBody);

        assertThat(response.getStatusLine().getStatusCode(), equalTo(400)); // Bad Request
        assertThat(responseBody, containsString("Missing 'index' parameter"));
    }

    public void testStatisticsEndpointInvalidIndex() throws IOException, ParseException {
        Request request = new Request("GET", "/_plugins/statistics/invalid_index");

        Response response = getRestClient().performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        logger.info("response body: {}", responseBody);

        assertThat(response.getStatusLine().getStatusCode(), equalTo(404)); // Not Found
        assertThat(responseBody, containsString("index_not_found_exception"));
    }

    private void indexTestData(String index, String testDataJson) throws IOException {
        Request request = new Request("PUT", "/" + index);
        getRestClient().performRequest(request);

        Request bulkRequest = new Request("POST", "/" + index + "/_bulk");
        bulkRequest.addParameter("refresh", "true");
        StringBuilder bulkData = new StringBuilder();
        String[] dataItems = testDataJson.split("}, \\{");
        for (String dataItem : dataItems) {
            bulkData.append("{\"index\": {}}\n");
            bulkData.append(dataItem.replace("[", "").replace("]", "")).append("\n");
        }
        bulkRequest.setJsonEntity(bulkData.toString());
        getRestClient().performRequest(bulkRequest);
    }
}