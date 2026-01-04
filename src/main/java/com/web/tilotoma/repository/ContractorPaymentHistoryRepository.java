package com.web.tilotoma.repository;

import com.web.tilotoma.entity.labour.ContractorPaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractorPaymentHistoryRepository extends JpaRepository<ContractorPaymentHistory, Long> {



}
