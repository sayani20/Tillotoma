package com.web.tilotoma.repository;

import com.web.tilotoma.entity.ContractorPayment;
import com.web.tilotoma.entity.ContractorPaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContractorPaymentHistoryRepository extends JpaRepository<ContractorPaymentHistory, Long> {



}
