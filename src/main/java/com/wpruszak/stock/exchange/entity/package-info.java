@XmlJavaTypeAdapters({
        @XmlJavaTypeAdapter(
                type = LocalDate.class,
                value = LocalDateAdapter.class
        ),
        @XmlJavaTypeAdapter(
                type = LocalDateTime.class,
                value = LocalDateTimeAdapter.class
        )
})
package com.wpruszak.stock.exchange.entity;

import com.wpruszak.stock.exchange.xml.LocalDateAdapter;
import com.wpruszak.stock.exchange.xml.LocalDateTimeAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import java.time.LocalDate;
import java.time.LocalDateTime;
