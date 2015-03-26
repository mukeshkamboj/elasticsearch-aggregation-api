package es;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

public class SearchAPIDemo {

    public static SearchResponse getSearchResponse(){
       /*return ClientFactory.getClient().prepareSearch(ESApiDemo.INDEX)
                .setTypes(ESApiDemo.INDEX_TYPE)
                .setQuery(QueryBuilders.termQuery("multi", "test"))             // Query
                .setPostFilter(FilterBuilders.rangeFilter("age").from(12).to(18))   // Filter
                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();*/
       System.out.println("Default 10 recodrs are displayed");
       BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
       
       RangeQueryBuilder rangeBuilder = QueryBuilders.rangeQuery("tradeInDate");
       
       boolQuery.must(rangeBuilder.from(1420050600000L));
       boolQuery.must(rangeBuilder.to(1427221800000L));

       //boolQuery.must(QueryBuilders.matchQuery("imei", "351850050032385"));
       
       
      /* boolQuery.must(QueryBuilders.constantScoreQuery(FilterBuilders.existsFilter("imei")));
       
       boolQuery.must(QueryBuilders.constantScoreQuery(FilterBuilders.missingFilter("imei")));
      */
       
       
       
       
       return ClientFactory.getClient().prepareSearch(ESApiDemo.INDEX).setTypes(ESApiDemo.INDEX_TYPE).setQuery(boolQuery).setSize(10) .execute().actionGet();
    }
    
}
