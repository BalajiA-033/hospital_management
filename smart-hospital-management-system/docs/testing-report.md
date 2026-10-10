# Automated Testing & Verification Report

## Test Suite Overview
Automated unit tests were written using **JUnit 5** framework targeting utility validations, password cryptography, appointment doctor scheduling constraints, and pharmacy stock validation rules.

## Test Summary Table

| Test Case Name | Target Class | Condition / Input Tested | Expected Outcome | Result |
|---|---|---|---|---|
| `testPasswordHashingAndVerification` | `PasswordUtil` | Valid string `"admin123Password#"` | BCrypt hash generated and verified successfully | **PASSED** |
| `testEmptyPasswordThrowsException` | `PasswordUtil` | Empty string `""` | `IllegalArgumentException` thrown | **PASSED** |
| `testEmailValidation` | `ValidationUtil` | Valid and malformed emails | Return `true` for valid, `false` for invalid | **PASSED** |
| `testPhoneValidation` | `ValidationUtil` | 10-digit phone and short string | Return `true` for 10 digits, `false` for short | **PASSED** |
| `testDateValidation` | `ValidationUtil` | ISO date string vs invalid string | Return `true` for `YYYY-MM-DD` | **PASSED** |
| `testDoctorOnLeaveBookingConflict` | `AppointmentService` | Doctor availability = `ON_LEAVE` | `IllegalStateException` thrown preventing booking | **PASSED** |
| `testInvalidTransactionQuantity` | `PharmacyService` | Negative dispense quantity `-5` | `IllegalArgumentException` thrown preventing negative stock | **PASSED** |

## Manual Verification Matrix

1. **Patient Registration:** Registered patient records successfully persist in the table and are filterable by search.
2. **Bed Concurrency Test:** Simulating simultaneous allocations for an occupied bed triggers transaction rollback and displays alert message.
3. **Low-Stock Alerting:** Medicines with stock count $\le$ reorder level automatically trigger red table row highlighting and increment dashboard alert KPI card.
