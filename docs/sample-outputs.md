# Sample Output Reports & Query Execution Logs

## 1. Dashboard KPI Summary Output
```text
+-----------------------------------------------------------------------+
|                       HOSPITAL DASHBOARD OVERVIEW                     |
|                                                                       |
|  [ Total Patients Registered ]       [ Active Doctor Directory ]      |
|              6                                   4                    |
|                                                                       |
|  [ Today's Appointments ]            [ Available Beds ]               |
|              3                                   5                    |
|                                                                       |
|  [ Occupied Beds ]                   [ Low Stock Medicine Alerts ]    |
|              3                                   3                    |
+-----------------------------------------------------------------------+
```

## 2. Low-Stock Medicine Report
```text
Query: SELECT medicine_id, name, category, stock_quantity, reorder_level, unit_price FROM medicines WHERE stock_quantity <= reorder_level;

+-------------+-----------------------+-------------------+---------------+---------------+----------------+
| Medicine ID | Name                  | Category          | Current Stock | Reorder Level | Unit Price ($) |
+-------------+-----------------------+-------------------+---------------+---------------+----------------+
|           3 | Ibuprofen 400mg       | Anti-inflammatory |            15 |            25 |           8.00 |
|           4 | Insulin Glargine      | Antidiabetic      |             8 |            15 |         450.00 |
|           6 | Cetirizine 10mg       | Antihistamine     |             5 |            20 |           4.50 |
+-------------+-----------------------+-------------------+---------------+---------------+----------------+
```

## 3. Hospital Bed Occupancy by Ward
```text
Query: SELECT ward_name, COUNT(*) AS total_beds, SUM(CASE WHEN is_occupied = TRUE THEN 1 ELSE 0 END) AS occupied_beds FROM hospital_beds GROUP BY ward_name;

+----------------------+------------+---------------+----------------+----------------+
| Ward Name            | Total Beds | Occupied Beds | Available Beds | Occupancy Rate |
+----------------------+------------+---------------+----------------+----------------+
| Critical Care Ward   |          2 |             1 |              1 |         50.00% |
| General Ward A       |          2 |             1 |              1 |         50.00% |
| Deluxe Private Wing  |          2 |             1 |              1 |         50.00% |
| Semi-Private Wing    |          2 |             0 |              2 |          0.00% |
+----------------------+------------+---------------+----------------+----------------+
```
