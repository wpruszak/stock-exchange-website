package com.wpruszak.stock.exchange.command;

import com.wpruszak.stock.exchange.entity.StockIndex;
import com.wpruszak.stock.exchange.net.BaseXClient;
import com.wpruszak.stock.exchange.repository.StockIndexRepository;
import com.wpruszak.stock.exchange.service.Downloader;
import com.wpruszak.stock.exchange.service.Extractor;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class Download implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(Download.class);

    private static final String URL_TEMPLATE = "http://www.bankier.pl/inwestowanie/profile/quote.html?symbol=";

    private static final String[] SYMBOLS = {
            "WIG20",
            "ALIOR",
            "ASSECOPOL",
            "BZWBK",
            "CCC",
            "CYFRPLSAT",
            "ENERGA",
            "EUROCASH",
            "JSW",
            "KGHM",
            "LOTOS",
            "LPP",
            "MBANK",
            "ORANGEPL",
            "PEKAO",
            "PGE",
            "PGNIG",
            "PKNORLEN",
            "PKOBP",
            "PZU",
            "TAURONPE"
    };

    private StockIndexRepository stockIndexRepository;

    private Jaxb2Marshaller marshaller;

    public Download(StockIndexRepository stockIndexRepository, Jaxb2Marshaller marshaller) {
        this.stockIndexRepository = stockIndexRepository;
        this.marshaller = marshaller;
    }

    @Override
    public void run(String... strings) throws Exception {

        Downloader downloader = new Downloader();
        LocalDateTime date = LocalDateTime.now();
        List<StockIndex> list = new ArrayList<>();

        for(String symbol: SYMBOLS) {
            Document document = downloader.getDocument(URL_TEMPLATE + symbol);
            Extractor extractor = new Extractor(document);

            StockIndex index = extractor.extractStockIndex();
            if(index == null) {
                continue;
            }

            index.setDate(date);
            this.stockIndexRepository.save(index);
            list.add(index);
        }
        this.stockIndexRepository.flush();

        this.saveToXml(list);
    }

    private void saveToXml(List<StockIndex> list) {

        try(BaseXClient session = new BaseXClient("localhost", 1984, "admin", "admin")) {
            for(StockIndex index: list) {
                StringWriter writer = new StringWriter();
                Result result = new StreamResult(writer);
                this.marshaller.marshal(index, result);
                session.execute("open stockexchange;");
                InputStream is = new ByteArrayInputStream(writer.toString().getBytes());
                session.add("stockex/indexes.xml", is);
            }
        } catch (IOException exception) {
            logger.error(exception.getMessage());
        }
    }
}
