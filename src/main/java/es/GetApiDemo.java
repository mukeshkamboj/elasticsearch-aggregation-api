package es;

import org.elasticsearch.action.get.GetResponse;

public class GetApiDemo {

    static GetResponse getAPI() {
        return ClientFactory.getClient().prepareGet(ESApiDemo.INDEX, ESApiDemo.INDEX_TYPE, "1000001814").execute().actionGet();
    }
}
