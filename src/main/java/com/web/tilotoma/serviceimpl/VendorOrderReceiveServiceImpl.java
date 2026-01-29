package com.web.tilotoma.serviceimpl;

import com.web.tilotoma.dto.*;
import com.web.tilotoma.entity.material.*;
import com.web.tilotoma.repository.*;
import com.web.tilotoma.service.VendorOrderReceiveService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class VendorOrderReceiveServiceImpl
        implements VendorOrderReceiveService {

    @Autowired
    private VendorOrderRepository orderRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private VendorOrderReceiveRepository receiveRepository;

    @Autowired
    private StockLedgerRepository stockLedgerRepository;

    @Autowired
    private VendorPaymentRepository paymentRepository;

    /**
     * ✅ Receive material against an order
     * - Save receive entry (with challan)
     * - Update stock ledger (IN)
     * - Optional vendor payment
     * - Calculate received / paid / outstanding
     */
    @Override
    @Transactional
    public MaterialReceiveResponseDto receiveMaterial(
            MaterialReceiveRequestDto request) {

        // 1️⃣ Validate Order
        VendorOrder order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Vendor vendor = order.getVendor();

        // 2️⃣ Receive material + Stock IN
        request.getItems().forEach(item -> {

            Material material = materialRepository
                    .findById(item.getMaterialId())
                    .orElseThrow(() ->
                            new RuntimeException("Material not found"));

            double amount = item.getQuantity() * item.getRate();

            // 🔹 Save receive entry
            VendorOrderReceive receive = VendorOrderReceive.builder()
                    .vendorOrder(order)
                    .material(material)
                    .receivedQuantity(item.getQuantity())
                    .receivedRate(item.getRate())
                    .receivedAmount(amount)
                    .challanNumber(request.getChallanNumber())
                    .orderReceivedType(request.getOrderReceivedType())
                    .remarks(request.getRemarks())
                    .receivedOn(LocalDateTime.now())
                    .build();

            receiveRepository.save(receive);

            // 🔹 Stock Ledger IN
            StockLedger ledger = StockLedger.builder()
                    .material(material)
                    .vendorOrder(order)
                    .txnType(StockTxnType.IN)
                    .quantity(item.getQuantity())
                    .rate(item.getRate())
                    .reference("ORDER_RECEIVE")
                    .txnDate(LocalDateTime.now())
                    .build();

            stockLedgerRepository.save(ledger);
        });

        // 3️⃣ Optional payment at receive time
        if (request.getPaidAmount() != null
                && request.getPaidAmount() > 0) {

            VendorPayment payment = VendorPayment.builder()
                    .vendor(vendor)
                    .vendorOrder(order)
                    .paidAmount(request.getPaidAmount())
                    .paymentMode(request.getPaymentMode())
                    .referenceNo(request.getPaymentRef())
                    .paidOn(LocalDateTime.now())
                    .build();

            paymentRepository.save(payment);
        }

        // ✅ 3️⃣.5 MARK ORDER AS RECEIVED (🔥 REQUIRED CHANGE)
        order.setReceivedOrder(true);
        orderRepository.save(order);

        // 4️⃣ Final calculation (SOURCE OF TRUTH)
        Double totalReceived =
                receiveRepository.getTotalReceivedAmount(order.getId());

        Double totalPaid =
                paymentRepository.getTotalPaidByOrder(order.getId());

        Double outstanding =
                totalReceived - totalPaid;

        // 5️⃣ Return proper response DTO
        return new MaterialReceiveResponseDto(
                order.getId(),
                totalReceived,
                totalPaid,
                outstanding
        );
    }

    // ✅ RECEIVE LIST BY ORDER
    @Override
    public List<VendorOrderReceiveDto.ReceiveResponse>
    getReceivedByOrder(Long orderId) {

        return receiveRepository.findByVendorOrder_Id(orderId)
                .stream()
                .map(r -> {
                    VendorOrderReceiveDto.ReceiveResponse res =
                            new VendorOrderReceiveDto.ReceiveResponse();

                    res.setReceiveId(r.getId());
                    res.setMaterialId(r.getMaterial().getId());
                    res.setMaterialName(r.getMaterial().getMaterialName());
                    res.setQuantity(r.getReceivedQuantity());
                    res.setRate(r.getReceivedRate());
                    res.setAmount(r.getReceivedAmount());
                    res.setChallanNumber(r.getChallanNumber());
                    res.setReceivedOn(r.getReceivedOn());

                    return res;
                }).toList();
    }

    public List<Map<String, Object>> getMaterialStockById(Long materialId) {

        List<StockLedger> ledgerList =
                stockLedgerRepository.findByMaterial_Id(materialId);

        List<Map<String, Object>> result = new ArrayList<>();

        for (StockLedger sl : ledgerList) {

            Material m = sl.getMaterial();

            Map<String, Object> map = new LinkedHashMap<>();

            // From Material
            map.put("material_id", m.getId());
            map.put("material_name", m.getMaterialName());
            map.put("unit", m.getUnit());
            map.put("brand", m.getBrand());

            // From StockLedger
            map.put("quantity", sl.getQuantity());
            map.put("txn_type", sl.getTxnType().name());
            map.put("txn_date", sl.getTxnDate());

            result.add(map);
        }

        return result;
    }


    /*@Override
    public List<VendorOrderReceiveResponseDto> getReceivedOrders(
            LocalDate fromDate,
            LocalDate toDate) {

        LocalDateTime fromDateTime = null;
        LocalDateTime toDateTime = null;

        if (fromDate != null) {
            fromDateTime = fromDate.atStartOfDay();
        }

        if (toDate != null) {
            toDateTime = toDate.atTime(LocalTime.MAX);
        }

        return receiveRepository.findReceivesBetweenDates(
                fromDateTime,
                toDateTime
        );
    }*/
    @Override
    public List<VendorOrderReceiveResponseDto> getReceivedOrders(
            LocalDate fromDate,
            LocalDate toDate) {

        LocalDateTime fromDateTime = null;
        LocalDateTime toDateTime = null;

        if (fromDate != null) {
            fromDateTime = fromDate.atStartOfDay();
        }

        if (toDate != null) {
            toDateTime = toDate.atTime(LocalTime.MAX);
        }

        return receiveRepository.findReceivesBetweenDates(
                fromDateTime,
                toDateTime
        );
    }

    @Override
    public List<ReceiveResponseDto> getReceives(
            Long materialId,
            LocalDate fromDate,
            LocalDate toDate
    ) {

        LocalDateTime start = fromDate.atStartOfDay();
        LocalDateTime end = toDate.atTime(23, 59, 59);

        return receiveRepository
                .findByMaterial_IdAndReceivedOnBetween(materialId, start, end)
                .stream()
                .map(r -> new ReceiveResponseDto(
                        r.getReceivedOn().toLocalDate(),

                        r.getVendorOrder().getVendor().getId(),
                        r.getVendorOrder().getVendor().getVendorName(),

                        r.getMaterial().getId(),
                        r.getMaterial().getMaterialName(),

                        r.getReceivedQuantity(),
                        r.getChallanNumber()
                ))
                .toList();
    }

}
