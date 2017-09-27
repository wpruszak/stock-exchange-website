package com.wpruszak.stock.exchange.repository;

import com.wpruszak.stock.exchange.entity.CompanyIndex;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyIndexRepository extends JpaRepository<CompanyIndex, Long> {
}
