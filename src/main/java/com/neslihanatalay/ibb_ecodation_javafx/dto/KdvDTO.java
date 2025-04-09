package com.neslihanatalay.ibb_ecodation_javafx.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class KdvDTO {

    private Integer id;

    private Double amount;

    private Double kdvRate;

    private Double kdvAmount;

    private Double totalAmount;

    private String receiptNumber;

    private LocalDate transactionDate;

    private String description;

    private String exportFormat;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Double getAmount() {
		return amount;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public Double getKdvRate() {
		return kdvRate;
	}
	
	public void setKdvRate(Double kdvRate) {
		this.kdvRate = kdvRate;
	}
	
	public Double getKdvAmount() {
		return kdvAmount;
	}
	
	public void setKdvAmount(Double kdvAmount) {
		this.kdvAmount = kdvAmount;
	}
	
	public Double getTotalAmount() {
		return totalAmount;
	}
	
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public String getReceiptNumber() {
		return receiptNumber;
	}
	
	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}
	
	public LocalDate getTransactionDate() {
		return transactionDate;
	}
	
	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getExportFormat() {
		return exportFormat;
	}
	
	public void setExportFormat(String exportFormat) {
		this.exportFormat = exportFormat;
	}

    public boolean isValid() {
        return amount != null && kdvRate != null && amount > 0 && kdvRate >= 0 && transactionDate != null;
    }

    public void calculateTotals() {
        this.kdvAmount = amount * kdvRate / 100;
        this.totalAmount = amount + this.kdvAmount;
    }
	
    public String toExportString() {
        return String.format("""
                Fiş No     : %s
                Tarih      : %s
                Açıklama   : %s
                Tutar      : %.2f ₺
                KDV Oranı  : %% %.1f
                KDV Tutarı : %.2f ₺
                Genel Toplam: %.2f ₺
                """,
                receiptNumber,
                transactionDate,
                description != null ? description : "-",
                amount,
                kdvRate,
                kdvAmount,
                totalAmount
        );
    }
}
