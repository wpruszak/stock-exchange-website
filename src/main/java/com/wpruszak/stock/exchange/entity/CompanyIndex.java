package com.wpruszak.stock.exchange.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes = { @Index(columnList = "ticker", name = "idx_ticker"), @Index(columnList = "name", name = "idx_name") })
public class CompanyIndex {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String ticker;

    @Column(precision = 10, scale = 2)
    private Double exchange;

    @Column(precision = 10, scale = 2)
    private Double escChange;

    @Column(precision = 10, scale = 2)
    private Double percentChange;

    @Column(precision = 10, scale = 4)
    private Double influenceOnIndexPercent;

    @Column(precision = 10, scale = 4)
    private Double turnoverSharePercent;

    private Long packet;

    @Column(precision = 10, scale = 2)
    private Double walletShare;

    private LocalDateTime date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Double getExchange() {
        return exchange;
    }

    public void setExchange(Double exchange) {
        this.exchange = exchange;
    }

    public Double getEscChange() {
        return escChange;
    }

    public void setEscChange(Double escChange) {
        this.escChange = escChange;
    }

    public Double getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(Double percentChange) {
        this.percentChange = percentChange;
    }

    public Double getInfluenceOnIndexPercent() {
        return influenceOnIndexPercent;
    }

    public void setInfluenceOnIndexPercent(Double influenceOnIndexPercent) {
        this.influenceOnIndexPercent = influenceOnIndexPercent;
    }

    public Double getTurnoverSharePercent() {
        return turnoverSharePercent;
    }

    public void setTurnoverSharePercent(Double turnoverSharePercent) {
        this.turnoverSharePercent = turnoverSharePercent;
    }

    public Long getPacket() {
        return packet;
    }

    public void setPacket(Long packet) {
        this.packet = packet;
    }

    public Double getWalletShare() {
        return walletShare;
    }

    public void setWalletShare(Double walletShare) {
        this.walletShare = walletShare;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CompanyIndex [id=" + id + ", name=" + name + ", ticker=" + ticker + ", exchange=" + exchange
                + ", escChange=" + escChange + ", percentChange=" + percentChange + ", influenceOnIndexPercent="
                + influenceOnIndexPercent + ", turnoverSharePercent=" + turnoverSharePercent + ", packet=" + packet
                + ", walletShare=" + walletShare + ", date=" + date + "]";
    }
}
