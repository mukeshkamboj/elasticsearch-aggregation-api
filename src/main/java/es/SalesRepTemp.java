package es;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filters.Filters;
import org.elasticsearch.search.aggregations.bucket.missing.Missing;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;

public class SalesRepTemp {

    public static void main(String[] args) throws Exception {
        for (int j = 0; j < 100; j++) {
            SearchResponse searchResponse = ClientFactory.getClient().prepareSearch()
                    .addAggregation(AggregationBuilders.cardinality("userName").field("userName")).execute()
                    .actionGet();

            long distinctSalesRepUsers = ((Cardinality) searchResponse.getAggregations().get("userName")).getValue();

            int startFrom = 0;
            for (int maxRecord = 0; maxRecord < distinctSalesRepUsers;) {
                startFrom = maxRecord;
                maxRecord += 15;
                Map<String, ApiSalesUserReportDTO> salesRepUserReportDTOs = new LinkedHashMap<>();
                searchResponse = ClientFactory
                        .getClient()
                        .prepareSearch()
                        .addAggregation(
                                AggregationBuilders.terms("userName").field("userName").order(Terms.Order.count(false))
                                        .size(maxRecord)).execute().actionGet();

                // System.out.println(searchResponse);
                Terms salesRepUser = searchResponse.getAggregations().get("userName");

                ApiSalesUserReportDTO tempApiSalesUserReportDTO = null;

                List<Terms.Bucket> buckets = salesRepUser.getBuckets();
                System.out.println("buckets size : " + buckets.size() + " : page umber : " + buckets.size() / 15);
                Filters discrepancyFilter = null;

                Terms.Bucket entry = null;

                while (buckets.size() > startFrom) {
                    entry = buckets.get(startFrom);

                    tempApiSalesUserReportDTO = new ApiSalesUserReportDTO();
                    tempApiSalesUserReportDTO.setTotalTrades(entry.getDocCount());
                    tempApiSalesUserReportDTO.setApiSalesRep(entry.getKey());
                    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                    boolQuery.must(QueryBuilders.matchQuery("userName", entry.getKey()));
                    searchResponse = ClientFactory
                            .getClient()
                            .prepareSearch()
                            .setQuery(boolQuery)
                            .addAggregation(AggregationBuilders.sum("discrepancyTotal").field("discrepancyTotal"))
                            .addAggregation(AggregationBuilders.missing("totalCancelation").field("status"))
                            .addAggregation(AggregationBuilders.sum("totalTiv").field("tradeInValue"))
                            .addAggregation(
                                    AggregationBuilders
                                            .filters("agg")
                                            .filter(FilterBuilders.existsFilter("discrepancyReason"))
                                            .subAggregation(AggregationBuilders.count("totalTrades").field("itemId"))
                                            .subAggregation(
                                                    AggregationBuilders.count("discrepanciesCount").field(
                                                            "itemDiscrepancyReasons.adjustmentReason"))).execute()
                            .actionGet();

                    tempApiSalesUserReportDTO.setTotalDiscrepancyValue(new BigDecimal(((Sum) searchResponse
                            .getAggregations().get("discrepancyTotal")).getValue()));
                    tempApiSalesUserReportDTO.setTotalTradeInValue(new BigDecimal(((Sum) searchResponse
                            .getAggregations().get("totalTiv")).getValue()));
                    tempApiSalesUserReportDTO.setTotalCancellations(((Missing) searchResponse.getAggregations().get(
                            "totalCancelation")).getDocCount());

                    discrepancyFilter = searchResponse.getAggregations().get("agg");
                    for (Filters.Bucket tempEntry : discrepancyFilter.getBuckets()) {
                        tempApiSalesUserReportDTO.setDevicesAdjusted(((ValueCount) tempEntry.getAggregations().get(
                                "totalTrades")).getValue());
                        tempApiSalesUserReportDTO.setTotalDiscrepancies(((ValueCount) tempEntry.getAggregations().get(
                                "discrepanciesCount")).getValue());
                    }

                    searchResponse = ClientFactory.getClient().prepareSearch().setQuery(boolQuery).setSize(1).execute()
                            .actionGet();

                    tempApiSalesUserReportDTO.setTradeInCompanyParentName((String) searchResponse.getHits().getAt(0)
                            .getSource().get("parentCompanyName"));
                    tempApiSalesUserReportDTO.setTradeInCompanyName((String) searchResponse.getHits().getAt(0)
                            .getSource().get("companyName"));
                    tempApiSalesUserReportDTO.setTradeInUserName((String) searchResponse.getHits().getAt(0).getSource()
                            .get("userName"));

                    salesRepUserReportDTOs.put(entry.getKey(), tempApiSalesUserReportDTO);
                    startFrom++;
                }
                System.out.println(salesRepUserReportDTOs);
            }
        }

        System.out.println("end");
    }

}
