package es;

import java.io.Serializable;
import java.math.BigDecimal;

public class ApiSalesUserReportDTO implements Serializable {

	private static final long serialVersionUID = 5441757387599703426L;
	private String tradeInUserName;
	private String apiSalesRep;
	private String tradeInCompanyName;
	private String tradeInCompanyParentName;
	private Long totalTrades;
	private BigDecimal totalTradeInValue;
	private Long devicesAdjusted;
	private Long totalDiscrepancies;
	private BigDecimal totalDiscrepancieValue;
	private Long totalCancellations;

	public String getTradeInUserName() {
		return tradeInUserName;
	}

	public void setTradeInUserName(String tradeInUserName) {
		this.tradeInUserName = tradeInUserName;
	}

	public String getApiSalesRep() {
		return apiSalesRep;
	}

	public void setApiSalesRep(String apiSalesRep) {
		this.apiSalesRep = apiSalesRep;
	}

	public String getTradeInCompanyName() {
		return tradeInCompanyName;
	}

	public void setTradeInCompanyName(String tradeInCompanyName) {
		this.tradeInCompanyName = tradeInCompanyName;
	}

	public String getTradeInCompanyParentName() {
		return tradeInCompanyParentName;
	}

	public void setTradeInCompanyParentName(String tradeInCompanyParentName) {
		this.tradeInCompanyParentName = tradeInCompanyParentName;
	}

	public Long getTotalTrades() {
		return totalTrades;
	}

	public void setTotalTrades(Long totalTrades) {
		this.totalTrades = totalTrades;
	}

	public BigDecimal getTotalTradeInValue() {
		return totalTradeInValue;
	}

	public void setTotalTradeInValue(BigDecimal totalTradeInValue) {
		this.totalTradeInValue = totalTradeInValue;
	}

	public Long getDevicesAdjusted() {
		return devicesAdjusted;
	}

	public void setDevicesAdjusted(Long devicesAdjusted) {
		this.devicesAdjusted = devicesAdjusted;
	}

	public Long getTotalDiscrepancies() {
		return totalDiscrepancies;
	}

	public void setTotalDiscrepancies(Long totalDiscrepancies) {
		this.totalDiscrepancies = totalDiscrepancies;
	}

	public BigDecimal getTotalDiscrepancyValue() {
		return totalDiscrepancieValue;
	}

	public void setTotalDiscrepancyValue(BigDecimal totalDiscrepancyValue) {
		this.totalDiscrepancieValue = totalDiscrepancyValue;
	}



	public Long getTotalCancellations() {
		return totalCancellations;
	}

	public void setTotalCancellations(Long totalCancellations) {
		this.totalCancellations = totalCancellations;
	}

	@Override
	public String toString() {
		return "ApiSalesUserReportDTO [tradeInUserName=" + tradeInUserName
				+ ", apiSalesRep=" + apiSalesRep + ", tradeInCompanyName="
				+ tradeInCompanyName + ", tradeInCompanyParentName="
				+ tradeInCompanyParentName + ", totalTrades=" + totalTrades
				+ ", totalTradeInValue=" + totalTradeInValue
				+ ", devicesAdjusted=" + devicesAdjusted
				+ ", totalDiscrepancies=" + totalDiscrepancies
				+ ", totalDiscrepancieValue=" + totalDiscrepancieValue
				+ ", totalCancellations=" + totalCancellations + "]";
	}

}
