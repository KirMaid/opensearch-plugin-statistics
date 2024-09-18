package org.opensearch.rest.action;

import com.google.gson.Gson;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.node.NodeClient;
import org.opensearch.core.rest.RestStatus;
import org.opensearch.rest.BaseRestHandler;
import org.opensearch.rest.BytesRestResponse;
import org.opensearch.rest.RestRequest;
import org.opensearch.rest.RestResponse;
import org.opensearch.rest.model.StatisticsResponse;
import org.opensearch.rest.model.Ups;
import org.opensearch.search.SearchHit;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.opensearch.search.fetch.subphase.FetchSourceContext;

import java.io.IOException;
import java.util.*;

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
                new Route(RestRequest.Method.GET, "/_plugins/statistics/{index}")
        );
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) throws IOException {
        return channel -> {
            String index = request.param("index");
            if (index == null || index.isEmpty()) {
                channel.sendResponse(new BytesRestResponse(RestStatus.BAD_REQUEST, "Missing 'index' parameter"));
                return;
            }

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(10000);
            searchSourceBuilder.fetchSource(
                    new FetchSourceContext(true, new String[]{
                            "ups_adv_battery_run_time_remaining",
                            "ups_adv_output_voltage",
                            "host"}, null));

            SearchRequest searchRequest = new SearchRequest(index);
            searchRequest.source(searchSourceBuilder);

            client.search(searchRequest, new RestResponseListener<>(channel) {
                @Override
                public RestResponse buildResponse(SearchResponse searchResponse) throws Exception {
                    List<Ups> statuses = new ArrayList<>();

                    for (SearchHit hit : searchResponse.getHits().getHits()) {
                        Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                        Ups ups = new Ups();
                        ups.setUpsAdvBatteryRunTimeRemaining(((Number) sourceAsMap.get("ups_adv_battery_run_time_remaining")).intValue());
                        ups.setUpsAdvOutputVoltage(((Number) sourceAsMap.get("ups_adv_output_voltage")).doubleValue());
                        ups.setHost((String) sourceAsMap.get("host"));
                        statuses.add(ups);
                    }

                    Double avgRunTime = statisticsService.calculateAverage(statuses, "upsAdvBatteryRunTimeRemaining");
                    Double maxOutputVoltage = statisticsService.findMaxValue(statuses, "upsAdvOutputVoltage");
                    Set<String> hosts = statisticsService.extractUniqueValues(statuses, "host");

                    StatisticsResponse response = new StatisticsResponse(avgRunTime, maxOutputVoltage, new ArrayList<>(hosts));
                    String jsonResponse = new Gson().toJson(response);
                    return new BytesRestResponse(RestStatus.OK, jsonResponse);
                }
            });
        };
    }
}
