package com.wpruszak.stock.exchange.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wpruszak.stock.exchange.entity.CompanyIndex;
import com.wpruszak.stock.exchange.entity.StockIndex;

public class Extractor {

    private static final int PROPERTY_COUNT_COMPANY = 9;
    private static final int PROPERTY_COUNT_INDEX_INFO = 10;
    private static final int PROPERTY_COUNT_INDEX_INFO_ALTERNATE = 11;

    private static final int INDEX_COMPANY_NAME = 0;
    private static final int INDEX_COMPANY_TICKER = 1;
    private static final int INDEX_COMPANY_EXCHANGE = 2;
    private static final int INDEX_COMPANY_CHANGE = 3;
    private static final int INDEX_COMPANY_PERCENT_CHANGE = 4;
    private static final int INDEX_COMPANY_INFLUENCE_ON_INDEX_PERCENT = 5;
    private static final int INDEX_COMPANY_TURNOVER_SHARE_PERCENT = 6;
    private static final int INDEX_COMPANY_PACKET = 7;
    private static final int INDEX_COMPANY_WALLET_SHARE = 8;

    private static final int INDEX_INFO_REFERENCE_RATE = 0;
    private static final int INDEX_INFO_OPENING_RATE = 1;
    private static final int INDEX_INFO_MAX_1D = 2;
    private static final int INDEX_INFO_MIN_1D = 3;
    private static final int INDEX_INFO_TRADING_VOLUME = 4;
    private static final int INDEX_INFO_TRADING_VALUE = 5;
    private static final int INDEX_INFO_TRANSACTION_COUNT = 6;
    private static final int INDEX_INFO_RETURN_RATE_PERCENT = 7;
    private static final int INDEX_INFO_MAX_1R = 8;
    private static final int INDEX_INFO_MIN_1R = 9;

    private static final int INDEX_CHANGE_PERCENT = 0;
    private static final int INDEX_CHANGE_VALUE = 1;

    private static final String SELECTOR_INDEX_INFO = "#boxProfil .boxContent.boxTable table.summaryTable tbody tr.startingData td.textBold";
    private static final String SELECTOR_INDEX_NAME = "#boxProfilHeader div.boxHeader .profilHead";
    private static final String SELECTOR_INDEX_EXCHANGE = "#boxProfilHeader div.boxHeader div.profilLast";
    private static final String SELECTOR_INDEX_CHANGES = "#boxProfilHeader div.boxHeader div.change span";
    private static final String SELECTOR_COMPANIES = "#boxProfil .boxContent.boxTable table.sortTableMixedData.floatingHeaderTable tbody";

    private Document document;

    public Extractor(Document document) {
        this.document = document;
    }

    public StockIndex extractStockIndex() {

        final Elements elements = document.select(SELECTOR_INDEX_INFO);
        if (elements.size() != PROPERTY_COUNT_INDEX_INFO && elements.size() != PROPERTY_COUNT_INDEX_INFO_ALTERNATE) {
            return null;
        }

        final StockIndex index = new StockIndex();
        index.setReferenceRate(this.extractDouble(elements.get(INDEX_INFO_REFERENCE_RATE).text()));
        index.setOpeningRate(this.extractDouble(elements.get(INDEX_INFO_OPENING_RATE).text()));
        index.setMax1D(this.extractDouble(elements.get(INDEX_INFO_MAX_1D).text()));
        index.setMin1D(this.extractDouble(elements.get(INDEX_INFO_MIN_1D).text()));
        index.setTradingVolume(this.extractLong(elements.get(INDEX_INFO_TRADING_VOLUME).text()));
        index.setTradingValue(this.extractLong(elements.get(INDEX_INFO_TRADING_VALUE).text()));
        index.setTransactionCount(this.extractLong(elements.get(INDEX_INFO_TRANSACTION_COUNT).text()));
        index.setReturnRatePercent(this.extractDouble(elements.get(INDEX_INFO_RETURN_RATE_PERCENT).text()));
        final String max1R = elements.get(INDEX_INFO_MAX_1R).text();
        index.setMax1R(this.extractDouble(max1R.substring(0, max1R.indexOf("("))));
        index.setMax1Rdate(this.extractDate(max1R.substring(max1R.indexOf("(") + 1, max1R.indexOf(")"))));
        final String min1R = elements.get(INDEX_INFO_MIN_1R).text();
        index.setMin1R(this.extractDouble(min1R.substring(0, min1R.indexOf("(") - 1)));
        index.setMin1Rdate(this.extractDate(min1R.substring(min1R.indexOf("(") + 1, min1R.indexOf(")"))));

        final String rawName = document.select(SELECTOR_INDEX_NAME).text();
        if (rawName.contains("(")) {
            index.setName(rawName.substring(0, rawName.indexOf("(")).trim());
            index.setTicker(rawName.substring(rawName.indexOf("(") + 1, rawName.indexOf(")")).trim());
        } else {
            index.setName(rawName);
            index.setTicker(rawName);
        }

        index.setExchange(this.extractDouble(document.select(SELECTOR_INDEX_EXCHANGE).text()));

        final Elements changeValues = document.select(SELECTOR_INDEX_CHANGES);
        index.setPercentChange(this.extractDouble(changeValues.get(INDEX_CHANGE_PERCENT).text()));
        index.setEscChange(this.extractDouble(changeValues.get(INDEX_CHANGE_VALUE).text()));

        return index;
    }

    public List<CompanyIndex> extractCompanyIndexes() {
        return document
                .select(SELECTOR_COMPANIES)
                .select("tr")
                .stream()
                .map(this::extractCompanyIndex)
                .filter(index -> index != null)
                .collect(Collectors.toList());
    }

    private CompanyIndex extractCompanyIndex(Element element) {

        final Elements elements = element.select("td");
        if (elements.size() != PROPERTY_COUNT_COMPANY) {
            return null;
        }

        final CompanyIndex index = new CompanyIndex();
        index.setName(elements.get(INDEX_COMPANY_NAME).text());
        index.setTicker(elements.get(INDEX_COMPANY_TICKER).text());
        index.setExchange(this.extractDouble(elements.get(INDEX_COMPANY_EXCHANGE).text()));
        index.setEscChange(this.extractDouble(elements.get(INDEX_COMPANY_CHANGE).text()));
        index.setPercentChange(this.extractDouble(elements.get(INDEX_COMPANY_PERCENT_CHANGE).text()));
        index.setInfluenceOnIndexPercent(this.extractDouble(elements.get(INDEX_COMPANY_INFLUENCE_ON_INDEX_PERCENT).text()));
        index.setTurnoverSharePercent(this.extractDouble(elements.get(INDEX_COMPANY_TURNOVER_SHARE_PERCENT).text()));
        index.setPacket(this.extractLong(elements.get(INDEX_COMPANY_PACKET).text()));
        index.setWalletShare(this.extractDouble(elements.get(INDEX_COMPANY_WALLET_SHARE).text()));

        return index;
    }

    private LocalDate extractDate(String rawDate) {
        return LocalDate.parse(rawDate);
    }

    private Long extractLong(String rawLong) {
        return Long.parseLong(rawLong.replaceAll("\\D+",""));
    }

    private Double extractDouble(String rawDouble) {
        return Double.parseDouble(rawDouble.replace(",", ".").replaceAll("[^0-9\\.\\-]", ""));
    }
}
