package com.hospital;

import com.hospital.dao.PharmacyDAO;
import com.hospital.model.PharmacyTransaction;
import com.hospital.service.PharmacyService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PharmacyServiceTest {

    @Test
    public void testInvalidTransactionQuantityThrowsException() {
        PharmacyService service = new PharmacyService(new PharmacyDAO());

        PharmacyTransaction tx = new PharmacyTransaction();
        tx.setMedicineId(1);
        tx.setTransactionType("DISPENSE");
        tx.setQuantity(-5); // Negative quantity

        assertThrows(IllegalArgumentException.class, () -> service.processStockTransaction(tx));
    }
}
