package com.web.tilotoma.repository;

import com.web.tilotoma.entity.labour.ContractorPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContractorPaymentRepository extends JpaRepository<ContractorPayment, Long> {

    List<ContractorPayment> findByBillDateBetween(LocalDate start, LocalDate end);

    List<ContractorPayment> findByContractorId(Long contractorId);

    List<ContractorPayment> findByContractorIdAndBillDateBetween(Long contractorId, LocalDate start, LocalDate end);

    Optional<ContractorPayment> findByContractorIdAndBillNo(Long contractorId, String billNo);
    Optional<ContractorPayment> findByBillNo(String billNo);

}
