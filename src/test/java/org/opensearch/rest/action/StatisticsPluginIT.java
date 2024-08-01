/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */
package org.opensearch.rest.action;

import com.carrotsearch.randomizedtesting.annotations.ThreadLeakScope;
import org.opensearch.client.Request;
import org.opensearch.client.Response;
import org.opensearch.plugins.Plugin;
import org.opensearch.test.OpenSearchIntegTestCase;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;

@ThreadLeakScope(ThreadLeakScope.Scope.NONE)
@OpenSearchIntegTestCase.ClusterScope(scope = OpenSearchIntegTestCase.Scope.SUITE)
public class StatisticsPluginIT extends OpenSearchIntegTestCase {

    @Override
    protected Collection<Class<? extends Plugin>> nodePlugins() {
        return Collections.singletonList(StatisticsPlugin.class);
    }

    public void testPluginInstalled() throws IOException {
        Response response = getRestClient().performRequest(new Request("GET", "/_cat/plugins"));
        String body = response.getEntity().toString();

        logger.info("response body: {}", body);
        assertThat(body, containsString("Statistics"));
    }
}
