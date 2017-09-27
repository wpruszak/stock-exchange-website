package com.wpruszak.stock.exchange.repository;

import com.wpruszak.stock.exchange.entity.StockIndex;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockIndexRepository extends JpaRepository<StockIndex, Long> {
}
