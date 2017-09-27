package com.wpruszak.stock.exchange.controller;

import com.wpruszak.stock.exchange.net.BaseXClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class SearchController {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @RequestMapping(value = {"/", "/search"}, method = RequestMethod.GET)
    public String indexAction() {
        return "index";
    }

    @RequestMapping(value = {"/", "/search"}, method = RequestMethod.POST)
    @ResponseBody
    public String searchAction(@RequestParam(name = "q", defaultValue = "") String query) {

        String result = "";

        if(query.equals(result)) {
            return  result;
        }

        try(BaseXClient session = new BaseXClient("localhost", 1984, "admin", "admin")) {
            session.execute("open stockexchange;");
            result = session.execute("xquery " + query);
        } catch (IOException exception) {
            result = exception.getMessage();
            logger.error(exception.getMessage());
        }

        return result;
    }
}
