package org.opensearch.rest.action;

import com.google.gson.Gson;
import org.opensearch.client.node.NodeClient;
import org.opensearch.core.rest.RestStatus;
import org.opensearch.rest.BaseRestHandler;
import org.opensearch.rest.BytesRestResponse;
import org.opensearch.rest.RestRequest;
import org.opensearch.rest.model.StatisticsResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.opensearch.rest.action.StatisticsService.*;

public class RestStatisticsAction extends BaseRestHandler {

    private final StatisticsService statisticsService;

    public RestStatisticsAction(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Override
    public String getName() {
        return "rest_handler_test";
    }


    @Override
    public List<Route> routes() {
        return Collections.singletonList(
                new Route(RestRequest.Method.GET, "/_plugins/statistics")
        );
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) throws IOException {
        return channel -> {
            String filePath = request.param("filePath");
            if (filePath == null || filePath.isEmpty()) {
                channel.sendResponse(new BytesRestResponse(RestStatus.BAD_REQUEST, "Missing 'filePath' parameter"));
                return;
            }
            try {
                StatisticsResponse response = statisticsService.processFile(filePath);
                String jsonResponse = new Gson().toJson(response);
                channel.sendResponse(new BytesRestResponse(RestStatus.OK, jsonResponse));
            } catch (IOException e) {
                channel.sendResponse(new BytesRestResponse(RestStatus.INTERNAL_SERVER_ERROR, "Error reading the JSON file: " + e.getMessage()));
            }
        };
    }
}
