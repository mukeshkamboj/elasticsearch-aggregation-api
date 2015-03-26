package es;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ClientFactory {

    private static Client client = null;
    
    static{
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "erc-es").build();
        client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("10.10.5.71", 9300));
    }
    
    public static Client getClient(){
        return client;
    }
}
