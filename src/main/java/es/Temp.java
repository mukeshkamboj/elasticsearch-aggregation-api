package es;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class Temp {

    public static void main(String[] args) throws Exception {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        List<Long> companyId = new ArrayList<Long>(){{add(1L);add(2L);}};
        
        
        List<String> companyType = new ArrayList<String>(){{add("ABC");add("CDE");}};
        
        //boolQuery.must(QueryBuilders.inQuery("companyId", companyId));
        
        boolQuery.must(QueryBuilders.matchQuery("companyStoreType",  companyId));


        SearchResponse searchResponse = ClientFactory.getClient().prepareSearch("temp").setQuery(boolQuery).

        get();

        System.out.println(searchResponse);
    }

}
