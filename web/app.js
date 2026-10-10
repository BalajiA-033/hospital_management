/**
 * AuraCare™ Smart Hospital Management System (SHMS)
 * Real-Time Client Application & State Management
 */

// ==========================================
// 1. Initial State & Data Seeding
// ==========================================

const INITIAL_DATA = {
  doctors: [
    { id: 1, name: "Dr. John Watson", dept: "Cardiology", qual: "MD, FACC", fee: 650, status: "AVAILABLE", phone: "555-0111", email: "watson@auracare.org" },
    { id: 2, name: "Dr. Sarah Jenkins", dept: "Neurology", qual: "MD, PhD", fee: 750, status: "AVAILABLE", phone: "555-0112", email: "jenkins@auracare.org" },
    { id: 3, name: "Dr. Robert Chen", dept: "Pediatrics", qual: "MD, FAAP", fee: 500, status: "BUSY", phone: "555-0113", email: "chen@auracare.org" },
    { id: 4, name: "Dr. Emily Davis", dept: "Orthopedics", qual: "MS, Ortho", fee: 700, status: "AVAILABLE", phone: "555-0114", email: "davis@auracare.org" },
    { id: 5, name: "Dr. Michael Taylor", dept: "General Medicine", qual: "MBBS, MD", fee: 450, status: "ON_LEAVE", phone: "555-0115", email: "taylor@auracare.org" },
    { id: 6, name: "Dr. Priya Sharma", dept: "Emergency Medicine", qual: "MD, FACEP", fee: 600, status: "AVAILABLE", phone: "555-0116", email: "sharma@auracare.org" }
  ],
  patients: [
    { id: 101, name: "James Wilson", gender: "MALE", dob: "1981-04-12", blood: "O+", phone: "555-0101", emergency: "555-0901", address: "44 Riverside Ave", bed: "ICU-101" },
    { id: 102, name: "Eleanor Vance", gender: "FEMALE", dob: "1994-08-23", blood: "A+", phone: "555-0102", emergency: "555-0902", address: "12 Pinecrest Blvd", bed: "GEN-201" },
    { id: 103, name: "David Miller", gender: "MALE", dob: "1968-11-05", blood: "B+", phone: "555-0103", emergency: "555-0903", address: "89 Oakwood Dr", bed: "ICU-103" },
    { id: 104, name: "Sophia Garcia", gender: "FEMALE", dob: "1999-02-17", blood: "AB+", phone: "555-0104", emergency: "555-0904", address: "302 Sunset Blvd", bed: "GEN-203" },
    { id: 105, name: "Liam Johnson", gender: "MALE", dob: "2018-06-30", blood: "O-", phone: "555-0105", emergency: "555-0905", address: "17 Maple St", bed: "EMR-301" },
    { id: 106, name: "Chloe Zhang", gender: "FEMALE", dob: "1962-09-14", blood: "A-", phone: "555-0106", emergency: "555-0906", address: "91 Harbor View", bed: "GEN-204" }
  ],
  beds: [
    { id: 1, number: "ICU-101", ward: "ICU", type: "ICU", charge: 3500, occupied: true, patientName: "James Wilson", patientId: 101 },
    { id: 2, number: "ICU-102", ward: "ICU", type: "ICU", charge: 3500, occupied: false, patientName: null, patientId: null },
    { id: 3, number: "ICU-103", ward: "ICU", type: "ICU", charge: 3500, occupied: true, patientName: "David Miller", patientId: 103 },
    { id: 4, number: "GEN-201", ward: "General Ward A", type: "GENERAL", charge: 800, occupied: true, patientName: "Eleanor Vance", patientId: 102 },
    { id: 5, number: "GEN-202", ward: "General Ward A", type: "GENERAL", charge: 800, occupied: false, patientName: null, patientId: null },
    { id: 6, number: "GEN-203", ward: "General Ward A", type: "GENERAL", charge: 800, occupied: true, patientName: "Sophia Garcia", patientId: 104 },
    { id: 7, number: "GEN-204", ward: "General Ward B", type: "GENERAL", charge: 800, occupied: true, patientName: "Chloe Zhang", patientId: 106 },
    { id: 8, number: "EMR-301", ward: "Emergency Ward", type: "EMERGENCY", charge: 1200, occupied: true, patientName: "Liam Johnson", patientId: 105 }
  ],
  emergencyCases: [
    { id: 1, patientName: "Liam Johnson", priority: "CRITICAL", condition: "Acute pediatric anaphylactic shock with stridor", doctor: "Dr. Priya Sharma", bed: "EMR-301", time: "10 mins ago" },
    { id: 2, patientName: "Unconscious Male (~35yr)", priority: "HIGH", condition: "Multiple vehicular blunt trauma, intracranial hemorrhage suspicion", doctor: "Dr. Emily Davis", bed: "Pending ICU", time: "25 mins ago" },
    { id: 3, patientName: "Martha Stewart (72yr)", priority: "MEDIUM", condition: "Hypertensive urgency BP 210/115, mild cephalalgia", doctor: "Dr. John Watson", bed: "GEN-201", time: "42 mins ago" },
    { id: 4, patientName: "Kevin Adams", priority: "LOW", condition: "Superficial forearm laceration, minor bleed arrested", doctor: "Dr. Michael Taylor", bed: "Outpatient", time: "1 hr ago" }
  ],
  medicines: [
    { id: 1, name: "Paracetamol 500mg", category: "Analgesic", price: 5.00, stock: 250, reorder: 50, manufacturer: "PharmaCorp", expiry: "2027-12-31" },
    { id: 2, name: "Insulin Glargine 100U", category: "Antidiabetic", price: 45.00, stock: 8, reorder: 15, manufacturer: "NovoCare", expiry: "2026-08-15" },
    { id: 3, name: "Amoxicillin 500mg", category: "Antibiotic", price: 12.50, stock: 85, reorder: 25, manufacturer: "MedTech Labs", expiry: "2027-05-10" },
    { id: 4, name: "Atorvastatin 20mg", category: "Cardiovascular", price: 18.00, stock: 6, reorder: 20, manufacturer: "Pfizer Global", expiry: "2026-11-20" },
    { id: 5, name: "Ibuprofen 400mg", category: "Analgesic", price: 7.50, stock: 140, reorder: 30, manufacturer: "Bayer Healthcare", expiry: "2028-01-15" },
    { id: 6, name: "Ceftriaxone 1g IV", category: "Antibiotic", price: 35.00, stock: 42, reorder: 15, manufacturer: "Roche Pharma", expiry: "2026-09-30" }
  ],
  appointments: [
    { id: 1, patient: "James Wilson", doctor: "Dr. John Watson", date: "2026-10-10", slot: "09:00 AM", reason: "Follow-up ECG & Cardiac rhythm evaluation", status: "COMPLETED" },
    { id: 2, patient: "Eleanor Vance", doctor: "Dr. Sarah Jenkins", date: "2026-10-10", slot: "11:30 AM", reason: "Migraine aura assessment & EEG review", status: "SCHEDULED" },
    { id: 3, patient: "Sophia Garcia", doctor: "Dr. Emily Davis", date: "2026-10-10", slot: "02:00 PM", reason: "Right knee ACL reconstruction post-op check", status: "SCHEDULED" }
  ],
  liveFeed: [
    { icon: "🚨", msg: "Emergency: Liam Johnson admitted under Triage CRITICAL", time: "Just now", type: "danger" },
    { icon: "🛏️", msg: "Bed EMR-301 locked & allocated to Liam Johnson", time: "2m ago", type: "info" },
    { icon: "💊", msg: "Dispensed 10x Paracetamol 500mg to James Wilson", time: "5m ago", type: "success" },
    { icon: "📅", msg: "Appointment confirmed: Eleanor Vance with Dr. Sarah Jenkins", time: "12m ago", type: "info" },
    { icon: "⚠️", msg: "Inventory Alert: Atorvastatin 20mg dropped below reorder level (6 left)", time: "18m ago", type: "warning" }
  ]
};

// Application State Loaded from LocalStorage or Defaults
let state = (function loadState() {
  const saved = localStorage.getItem("auracare_state_v1");
  if (saved) {
    try {
      return JSON.parse(saved);
    } catch (e) {
      console.error("State parse error, reverting to defaults", e);
    }
  }
  return JSON.parse(JSON.stringify(INITIAL_DATA));
})();

function saveState() {
  localStorage.setItem("auracare_state_v1", JSON.stringify(state));
  updateKpiBadges();
}

function resetDemoData() {
  if (confirm("Reset all hospital records back to original demo database?")) {
    state = JSON.parse(JSON.stringify(INITIAL_DATA));
    saveState();
    renderAllPanels();
    showToast("Database successfully reverted to initial seed records", "info");
  }
}

// ==========================================
// 2. Navigation & View Controller
// ==========================================

const TABS = {
  dashboard: { title: "Clinical Command Center", subtitle: "Real-Time Hospital Telemetry & Synchronized Operations" },
  doctors: { title: "Doctor Directory & Rota", subtitle: "Active Consultants, Department Specializations & Scheduling" },
  beds: { title: "Bed Management & Ward Grid", subtitle: "Transactional Bed Allocation & Row-Level Locking (FOR UPDATE)" },
  emergency: { title: "Emergency Triage Board", subtitle: "Dynamic Level 1 to 4 Intake & Doctor Assignment Queues" },
  patients: { title: "Patient Registry (EHR)", subtitle: "Electronic Health Records & Clinical Admission Histories" },
  pharmacy: { title: "Pharmacy & Dispensary Inventory", subtitle: "Real-Time Stock Alerts, Non-Negative Stock Checks & Dispensing" },
  reports: { title: "DBMS SQL Analytical Reports", subtitle: "Automated Relational Aggregations, GROUP BYs & Complex Subqueries" }
};

function switchTab(tabKey) {
  // Update Nav buttons
  document.querySelectorAll(".nav-item").forEach(item => item.classList.remove("active"));
  const btn = document.getElementById(`navBtn${tabKey.charAt(0).toUpperCase() + tabKey.slice(1)}`);
  if (btn && btn.parentElement) {
    btn.parentElement.classList.add("active");
  }

  // Update Tab panels
  document.querySelectorAll(".tab-panel").forEach(panel => panel.classList.remove("active"));
  const activePanel = document.getElementById(`tab${tabKey.charAt(0).toUpperCase() + tabKey.slice(1)}`);
  if (activePanel) {
    activePanel.classList.add("active");
  }

  // Update Headings
  const meta = TABS[tabKey] || TABS.dashboard;
  document.getElementById("pageHeadingTitle").textContent = meta.title;
  document.getElementById("pageSubtitleText").textContent = meta.subtitle;

  // Refresh relevant panels
  if (tabKey === "dashboard") renderDashboard();
  if (tabKey === "doctors") renderDoctors();
  if (tabKey === "beds") renderBeds();
  if (tabKey === "emergency") renderEmergency();
  if (tabKey === "patients") renderPatients();
  if (tabKey === "pharmacy") renderPharmacy();
  if (tabKey === "reports") loadDbmsReport(1);
}

// Role Switcher simulation
function switchRole(role) {
  const avatar = document.getElementById("currentUserAvatar");
  const name = document.getElementById("currentUserName");
  const roleText = document.getElementById("currentUserRole");

  if (role === "admin") {
    avatar.textContent = "AD";
    name.textContent = "Admin (Root)";
    roleText.textContent = "Full Clinical Access";
    showToast("Switched to System Administrator role (All permissions)", "info");
  } else if (role === "doctor") {
    avatar.textContent = "JW";
    name.textContent = "Dr. John Watson";
    roleText.textContent = "Consultant Cardiologist";
    showToast("Switched to Doctor perspective: Dr. John Watson", "info");
  } else if (role === "staff") {
    avatar.textContent = "SM";
    name.textContent = "Staff Nurse Mary";
    roleText.textContent = "Ward & Dispensary Staff";
    showToast("Switched to Hospital Staff role: Mary", "info");
  }
}

// Theme Toggle
function toggleTheme() {
  const current = document.documentElement.getAttribute("data-theme") || "dark";
  const next = current === "dark" ? "light" : "dark";
  document.documentElement.setAttribute("data-theme", next);
  localStorage.setItem("auracare_theme", next);
  showToast(`Theme switched to ${next.toUpperCase()} mode`, "info");
}

(function initTheme() {
  const saved = localStorage.getItem("auracare_theme");
  if (saved) document.documentElement.setAttribute("data-theme", saved);
})();

// Real-time Clock
function updateClock() {
  const now = new Date();
  const timeStr = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' });
  const dateStr = now.toLocaleDateString([], { month: 'short', day: 'numeric' });
  const el = document.getElementById("liveClockText");
  if (el) el.textContent = `${timeStr} (${dateStr})`;
}
setInterval(updateClock, 1000);
updateClock();

// ==========================================
// 3. Panel Renderers
// ==========================================

function updateKpiBadges() {
  const totalPatients = state.patients.length;
  const activeDocs = state.doctors.filter(d => d.status === "AVAILABLE").length;
  const occupiedBeds = state.beds.filter(b => b.occupied).length;
  const totalBeds = state.beds.length;
  const bedRate = Math.round((occupiedBeds / totalBeds) * 100);
  const critCases = state.emergencyCases.filter(c => c.priority === "CRITICAL").length;
  const lowStock = state.medicines.filter(m => m.stock <= m.reorder).length;

  // KPI Numbers
  const elPatients = document.getElementById("kpiPatientsCount");
  if (elPatients) elPatients.textContent = totalPatients;

  const elDocs = document.getElementById("kpiDoctorsActive");
  if (elDocs) elDocs.textContent = `${activeDocs} / ${state.doctors.length}`;

  const elBedsRate = document.getElementById("kpiBedsRate");
  if (elBedsRate) elBedsRate.textContent = `${bedRate}%`;

  const elBedsRatio = document.getElementById("kpiBedsRatio");
  if (elBedsRatio) elBedsRatio.textContent = `${occupiedBeds} / ${totalBeds} Beds`;

  const elBedsProgress = document.getElementById("kpiBedsProgressBar");
  if (elBedsProgress) elBedsProgress.style.width = `${bedRate}%`;

  const elEmergency = document.getElementById("kpiEmergencyCount");
  if (elEmergency) elEmergency.textContent = critCases;

  const elLowStock = document.getElementById("kpiLowStockCount");
  if (elLowStock) elLowStock.textContent = lowStock;

  const elAppts = document.getElementById("kpiAppointmentsCount");
  if (elAppts) elAppts.textContent = state.appointments.length;

  // Nav badges
  const navBed = document.getElementById("navBedBadge");
  if (navBed) navBed.textContent = `${bedRate}%`;

  const navEmer = document.getElementById("navEmergencyBadge");
  if (navEmer) navEmer.textContent = `${critCases} Crit`;

  const navPharm = document.getElementById("navPharmBadge");
  if (navPharm) navPharm.textContent = `${lowStock} Low`;
}

// 3.1 DASHBOARD
function renderDashboard() {
  updateKpiBadges();

  // Render Ward Breakdown Progress Bars
  const wardContainer = document.getElementById("wardProgressContainer");
  if (wardContainer) {
    const wards = ["ICU", "General Ward A", "General Ward B", "Emergency Ward"];
    let html = "";
    wards.forEach(w => {
      const wardBeds = state.beds.filter(b => b.ward.includes(w) || (w === "ICU" && b.ward === "ICU"));
      const total = wardBeds.length;
      const occupied = wardBeds.filter(b => b.occupied).length;
      const pct = total > 0 ? Math.round((occupied / total) * 100) : 0;
      const color = pct >= 80 ? "var(--danger)" : pct >= 50 ? "var(--warning)" : "var(--success)";

      html += `
        <div style="display: flex; flex-direction: column; gap: 6px;">
          <div style="display: flex; justify-content: space-between; font-size: 0.85rem; font-weight: 600;">
            <span>${w}</span>
            <span style="color: ${color};">${occupied} / ${total} Beds (${pct}%)</span>
          </div>
          <div style="height: 8px; width: 100%; background: rgba(255, 255, 255, 0.06); border-radius: 999px; overflow: hidden;">
            <div style="height: 100%; width: ${pct}%; background: ${color}; border-radius: 999px; transition: width 0.8s ease;"></div>
          </div>
        </div>
      `;
    });
    wardContainer.innerHTML = html;
  }

  // Render Live Feed
  const feedList = document.getElementById("dashboardLiveFeed");
  if (feedList) {
    feedList.innerHTML = state.liveFeed.map(item => `
      <div class="feed-item">
        <div class="feed-icon" style="background: rgba(255, 255, 255, 0.05);">${item.icon}</div>
        <div class="feed-details">
          <div class="feed-msg">${item.msg}</div>
          <div class="feed-time">${item.time}</div>
        </div>
      </div>
    `).join("");
  }
}

// 3.2 DOCTORS
let currentDeptFilter = "ALL";
function filterDoctorsByDept(dept, btn) {
  currentDeptFilter = dept;
  document.querySelectorAll("#doctorDeptFilters .filter-pill").forEach(p => p.classList.remove("active"));
  if (btn) btn.classList.add("active");
  renderDoctors();
}

function renderDoctors() {
  const grid = document.getElementById("doctorCardsGrid");
  if (!grid) return;

  const filtered = currentDeptFilter === "ALL" 
    ? state.doctors 
    : state.doctors.filter(d => d.dept.toLowerCase().includes(currentDeptFilter.toLowerCase()));

  grid.innerHTML = filtered.map(doc => {
    const isAvail = doc.status === "AVAILABLE";
    const statusColor = isAvail ? "var(--success)" : doc.status === "BUSY" ? "var(--warning)" : "var(--text-muted)";
    const initials = doc.name.replace("Dr. ", "").split(" ").map(w => w[0]).join("");

    return `
      <div class="doctor-card">
        <div class="doctor-avatar-circle">
          ${initials}
          <span class="doctor-status-dot" style="background: ${statusColor};"></span>
        </div>
        <div>
          <div class="doctor-name">${doc.name}</div>
          <div class="doctor-spec">${doc.dept}</div>
          <div class="doctor-qual">${doc.qual} • ${doc.phone}</div>
        </div>
        <div class="doctor-fee">$${doc.fee} / consult</div>
        <div style="font-size: 0.75rem; font-weight: 700; color: ${statusColor};">
          ● ${doc.status.replace("_", " ")}
        </div>
        <div class="doctor-card-actions">
          <button class="btn-primary btn-sm" onclick="openAppointmentForDoctor(${doc.id})">Book Slot</button>
          <button class="btn-secondary btn-sm" onclick="toggleDoctorStatus(${doc.id})">Toggle Duty</button>
        </div>
      </div>
    `;
  }).join("");
}

function toggleDoctorStatus(docId) {
  const doc = state.doctors.find(d => d.id === docId);
  if (!doc) return;
  const statuses = ["AVAILABLE", "BUSY", "ON_LEAVE"];
  const nextIdx = (statuses.indexOf(doc.status) + 1) % statuses.length;
  doc.status = statuses[nextIdx];
  saveState();
  renderDoctors();
  showToast(`${doc.name} status updated to ${doc.status}`, "info");
}

// 3.3 BEDS
let currentBedFilter = "ALL";
function filterBeds(filterKey, btn) {
  currentBedFilter = filterKey;
  document.querySelectorAll("#bedWardFilters .filter-pill").forEach(p => p.classList.remove("active"));
  if (btn) btn.classList.add("active");
  renderBeds();
}

function renderBeds() {
  const container = document.getElementById("bedGridContainer");
  if (!container) return;

  let filtered = state.beds;
  if (currentBedFilter === "ICU") filtered = state.beds.filter(b => b.ward === "ICU");
  else if (currentBedFilter === "GENERAL") filtered = state.beds.filter(b => b.ward.includes("General"));
  else if (currentBedFilter === "EMERGENCY") filtered = state.beds.filter(b => b.ward.includes("Emergency"));
  else if (currentBedFilter === "AVAILABLE_ONLY") filtered = state.beds.filter(b => !b.occupied);

  const occ = state.beds.filter(b => b.occupied).length;
  const tot = state.beds.length;
  const statText = document.getElementById("bedOccupancyStatsText");
  if (statText) statText.innerHTML = `Showing <b>${filtered.length}</b> beds | Overall Occupancy: <b>${occ}/${tot}</b> (${Math.round((occ/tot)*100)}%)`;

  container.innerHTML = filtered.map(bed => {
    return `
      <div class="bed-card ${bed.occupied ? 'status-occupied' : 'status-available'}">
        <div class="bed-card-header">
          <div>
            <div class="bed-number">${bed.number}</div>
            <div class="bed-ward">${bed.ward}</div>
          </div>
          <span class="bed-type-badge">${bed.type} • $${bed.charge}/d</span>
        </div>

        <div class="bed-occupant-info">
          ${bed.occupied ? `
            <div class="occupant-name" style="color: var(--danger);">Occupied by: ${bed.patientName}</div>
            <div class="occupant-details">Patient ID: #${bed.patientId} • Daily Monitor Active</div>
          ` : `
            <div class="occupant-name" style="color: var(--success);">Available for Admission</div>
            <div class="occupant-details">Sanitized & Sterilized • Ready for Assignment</div>
          `}
        </div>

        <div class="bed-actions">
          ${bed.occupied ? `
            <button class="btn-danger btn-sm" style="width: 100%;" onclick="dischargeBed(${bed.id})">Discharge Patient</button>
          ` : `
            <button class="btn-primary btn-sm" style="width: 100%;" onclick="openAllocateBedModal(${bed.id})">Allocate Bed</button>
          `}
        </div>
      </div>
    `;
  }).join("");
}

function openAllocateBedModal(bedId) {
  const bed = state.beds.find(b => b.id === bedId);
  if (!bed) return;
  document.getElementById("allocBedId").value = bed.id;
  document.getElementById("allocBedDisplay").value = `${bed.number} (${bed.ward} - $${bed.charge}/day)`;

  // Populate patient select with patients not currently in beds
  const select = document.getElementById("allocPatientSelect");
  select.innerHTML = state.patients.map(p => {
    const isCurrent = p.bed ? ` (Currently in ${p.bed})` : "";
    return `<option value="${p.id}">${p.name} (#${p.id}) - ${p.blood}${isCurrent}</option>`;
  }).join("");

  openModal("modalAllocateBed");
}

function handleBedAllocationSubmit(e) {
  e.preventDefault();
  const bedId = parseInt(document.getElementById("allocBedId").value);
  const patientId = parseInt(document.getElementById("allocPatientSelect").value);
  const notes = document.getElementById("allocNotes").value;

  const bed = state.beds.find(b => b.id === bedId);
  const patient = state.patients.find(p => p.id === patientId);

  if (!bed || !patient) return;

  // Execute row-level atomic lock simulation
  bed.occupied = true;
  bed.patientId = patient.id;
  bed.patientName = patient.name;
  patient.bed = bed.number;

  // Add to live feed
  state.liveFeed.unshift({
    icon: "🛏️",
    msg: `Bed ${bed.number} allocated to ${patient.name} (Atomic TX Completed)`,
    time: "Just now",
    type: "info"
  });

  saveState();
  closeModal("modalAllocateBed");
  renderBeds();
  renderDashboard();
  showToast(`Bed ${bed.number} successfully assigned to ${patient.name}`, "success");
}

function dischargeBed(bedId) {
  const bed = state.beds.find(b => b.id === bedId);
  if (!bed) return;

  if (confirm(`Confirm clinical discharge for patient ${bed.patientName} from ${bed.number}?`)) {
    const patient = state.patients.find(p => p.id === bed.patientId);
    if (patient) patient.bed = null;

    const formerName = bed.patientName;
    bed.occupied = false;
    bed.patientId = null;
    bed.patientName = null;

    state.liveFeed.unshift({
      icon: "✅",
      msg: `Patient ${formerName} discharged from ${bed.number}. Bed sanitized.`,
      time: "Just now",
      type: "success"
    });

    saveState();
    renderBeds();
    renderDashboard();
    showToast(`${bed.number} released & ready for new patient`, "info");
  }
}

// 3.4 EMERGENCY & TRIAGE
function renderEmergency() {
  const priorities = ["CRITICAL", "HIGH", "MEDIUM", "LOW"];
  priorities.forEach(p => {
    const listEl = document.getElementById(`triageList${p.charAt(0) + p.slice(1).toLowerCase()}`);
    const badgeEl = document.getElementById(`badge${p.charAt(0) + p.slice(1).toLowerCase()}Count`);
    const cases = state.emergencyCases.filter(c => c.priority === p);

    if (badgeEl) badgeEl.textContent = cases.length;
    if (listEl) {
      if (cases.length === 0) {
        listEl.innerHTML = `<div style="color: var(--text-muted); font-size: 0.8rem; text-align: center; padding: 20px 0;">No ${p} priority patients queued</div>`;
      } else {
        listEl.innerHTML = cases.map(c => `
          <div class="triage-item-card">
            <div class="triage-patient-title">${c.patientName}</div>
            <div class="triage-symptoms">${c.condition}</div>
            <div class="triage-meta-row">
              <span>👨‍⚕️ ${c.doctor || 'Unassigned'}</span>
              <span>🛏️ ${c.bed || 'Awaiting Bed'}</span>
            </div>
            <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 6px;">
              <span style="font-size: 0.7rem; color: var(--text-muted);">${c.time}</span>
              <button class="btn-sm btn-secondary" onclick="resolveEmergencyCase(${c.id})">Mark Stabilized</button>
            </div>
          </div>
        `).join("");
      }
    }
  });
}

function openEmergencyModal() {
  const docSelect = document.getElementById("emDoctorSelect");
  docSelect.innerHTML = `<option value="">-- Auto-assign on-duty ER Doctor --</option>` + 
    state.doctors.map(d => `<option value="${d.name}">${d.name} (${d.dept})</option>`).join("");
  openModal("modalEmergency");
}

function handleEmergencySubmit(e) {
  e.preventDefault();
  const name = document.getElementById("emPatientName").value;
  const priority = document.getElementById("emTriagePriority").value;
  const condition = document.getElementById("emCondition").value;
  const doctor = document.getElementById("emDoctorSelect").value || "Dr. Priya Sharma (ER)";

  const newCase = {
    id: Date.now(),
    patientName: name,
    priority: priority,
    condition: condition,
    doctor: doctor,
    bed: priority === "CRITICAL" ? "Pending ICU" : "ER Bay",
    time: "Just now"
  };

  state.emergencyCases.unshift(newCase);
  state.liveFeed.unshift({
    icon: "🚨",
    msg: `Emergency Admission: ${name} (Priority: ${priority})`,
    time: "Just now",
    type: "danger"
  });

  saveState();
  closeModal("modalEmergency");
  renderEmergency();
  renderDashboard();
  showToast(`Emergency patient ${name} registered in ${priority} triage queue!`, "danger");
}

function resolveEmergencyCase(caseId) {
  const idx = state.emergencyCases.findIndex(c => c.id === caseId);
  if (idx !== -1) {
    const patientName = state.emergencyCases[idx].patientName;
    state.emergencyCases.splice(idx, 1);
    state.liveFeed.unshift({
      icon: "🩺",
      msg: `Emergency patient ${patientName} treated & stabilized.`,
      time: "Just now",
      type: "success"
    });
    saveState();
    renderEmergency();
    renderDashboard();
    showToast(`Patient ${patientName} marked stabilized`, "success");
  }
}

// 3.5 PATIENTS
let currentBloodFilter = "ALL";
function filterPatientsByBlood(group, btn) {
  currentBloodFilter = group;
  document.querySelectorAll("#patientBloodFilters .filter-pill").forEach(p => p.classList.remove("active"));
  if (btn) btn.classList.add("active");
  renderPatients();
}

function renderPatients() {
  const tbody = document.getElementById("patientsTableBody");
  if (!tbody) return;

  const filtered = currentBloodFilter === "ALL" 
    ? state.patients 
    : state.patients.filter(p => p.blood === currentBloodFilter);

  tbody.innerHTML = filtered.map(p => `
    <tr>
      <td><span style="font-family: monospace; font-weight: 700; color: var(--primary);">#${p.id}</span></td>
      <td>
        <div style="font-weight: 600;">${p.name}</div>
        <div style="font-size: 0.74rem; color: var(--text-muted);">${p.address || 'Address unrecorded'}</div>
      </td>
      <td>${p.gender} (${calculateAge(p.dob)} yrs)</td>
      <td><span class="nav-badge badge-danger">${p.blood}</span></td>
      <td>${p.phone}</td>
      <td>${p.emergency}</td>
      <td>
        ${p.bed ? `<span class="nav-badge badge-warning">🛏️ ${p.bed}</span>` : `<span class="nav-badge badge-success">Outpatient</span>`}
      </td>
      <td>
        <button class="btn-sm btn-secondary" onclick="openAppointmentForPatient(${p.id})">Consult</button>
      </td>
    </tr>
  `).join("");
}

function calculateAge(dobStr) {
  if (!dobStr) return "--";
  const birthDate = new Date(dobStr);
  const diff = Date.now() - birthDate.getTime();
  const ageDate = new Date(diff);
  return Math.abs(ageDate.getUTCFullYear() - 1970);
}

function openPatientModal() {
  openModal("modalPatient");
}

function handleNewPatientSubmit(e) {
  e.preventDefault();
  const first = document.getElementById("pFirstName").value.trim();
  const last = document.getElementById("pLastName").value.trim();
  const gender = document.getElementById("pGender").value;
  const blood = document.getElementById("pBloodGroup").value;
  const dob = document.getElementById("pDob").value;
  const phone = document.getElementById("pPhone").value.trim();
  const emergency = document.getElementById("pEmergencyContact").value.trim();
  const address = document.getElementById("pAddress").value.trim();

  const newPatient = {
    id: 100 + state.patients.length + 1,
    name: `${first} ${last}`,
    gender: gender,
    dob: dob,
    blood: blood,
    phone: phone,
    emergency: emergency,
    address: address,
    bed: null
  };

  state.patients.push(newPatient);
  state.liveFeed.unshift({
    icon: "👤",
    msg: `New Patient Registered: ${newPatient.name} (PID #${newPatient.id})`,
    time: "Just now",
    type: "info"
  });

  saveState();
  closeModal("modalPatient");
  renderPatients();
  renderDashboard();
  showToast(`Patient ${newPatient.name} registered successfully!`, "success");
}

// 3.6 PHARMACY
let currentPharmCategory = "ALL";
function filterPharmacy(cat, btn) {
  currentPharmCategory = cat;
  document.querySelectorAll("#pharmacyCategoryFilters .filter-pill").forEach(p => p.classList.remove("active"));
  if (btn) btn.classList.add("active");
  renderPharmacy();
}

function renderPharmacy() {
  const tbody = document.getElementById("pharmacyTableBody");
  if (!tbody) return;

  let filtered = state.medicines;
  if (currentPharmCategory === "LOW_STOCK") filtered = state.medicines.filter(m => m.stock <= m.reorder);
  else if (currentPharmCategory !== "ALL") filtered = state.medicines.filter(m => m.category === currentPharmCategory);

  tbody.innerHTML = filtered.map(med => {
    const isLow = med.stock <= med.reorder;
    return `
      <tr class="${isLow ? 'row-alert' : ''}">
        <td><strong style="color: var(--text-primary);">${med.name}</strong></td>
        <td><span class="nav-badge badge-primary">${med.category}</span></td>
        <td><strong>$${med.price.toFixed(2)}</strong></td>
        <td>
          <span style="font-weight: 700; color: ${isLow ? 'var(--danger)' : 'var(--text-primary)'};">
            ${med.stock} units
          </span>
        </td>
        <td>${med.reorder} units</td>
        <td>${med.manufacturer}</td>
        <td>${med.expiry}</td>
        <td>
          ${isLow ? `
            <span class="nav-badge badge-danger">⚠️ REORDER NEEDED</span>
          ` : `
            <span class="nav-badge badge-success">STOCKED</span>
          `}
        </td>
        <td>
          <button class="btn-sm btn-primary" onclick="openDispenseForMed(${med.id})">Dispense</button>
        </td>
      </tr>
    `;
  }).join("");
}

function openDispenseModal() {
  const medSelect = document.getElementById("dispenseMedSelect");
  medSelect.innerHTML = state.medicines.map(m => `
    <option value="${m.id}" data-price="${m.price}" data-stock="${m.stock}">
      ${m.name} ($${m.price.toFixed(2)} - Stock: ${m.stock})
    </option>
  `).join("");

  const patSelect = document.getElementById("dispensePatientSelect");
  patSelect.innerHTML = state.patients.map(p => `<option value="${p.id}">${p.name} (#${p.id})</option>`).join("");

  updateDispenseCalculation();
  openModal("modalDispense");
}

function openDispenseForMed(medId) {
  openDispenseModal();
  document.getElementById("dispenseMedSelect").value = medId;
  updateDispenseCalculation();
}

function updateDispenseCalculation() {
  const sel = document.getElementById("dispenseMedSelect");
  const opt = sel.options[sel.selectedIndex];
  const qty = parseInt(document.getElementById("dispenseQty").value) || 1;
  const costEl = document.getElementById("dispenseTotalCostDisplay");

  if (opt) {
    const price = parseFloat(opt.getAttribute("data-price")) || 0;
    const total = price * qty;
    costEl.value = `$${total.toFixed(2)}`;
  }
}

function handleDispenseSubmit(e) {
  e.preventDefault();
  const medId = parseInt(document.getElementById("dispenseMedSelect").value);
  const patientId = parseInt(document.getElementById("dispensePatientSelect").value);
  const qty = parseInt(document.getElementById("dispenseQty").value);

  const med = state.medicines.find(m => m.id === medId);
  const patient = state.patients.find(p => p.id === patientId);

  if (!med || !patient) return;

  if (med.stock < qty) {
    showToast(`Insufficient stock! Only ${med.stock} units of ${med.name} available.`, "danger");
    return;
  }

  // Atomic deduction
  med.stock -= qty;
  const totalBill = med.price * qty;

  state.liveFeed.unshift({
    icon: "💊",
    msg: `Dispensed ${qty}x ${med.name} to ${patient.name} ($${totalBill.toFixed(2)})`,
    time: "Just now",
    type: "success"
  });

  saveState();
  closeModal("modalDispense");
  renderPharmacy();
  renderDashboard();
  showToast(`Successfully dispensed ${qty} units of ${med.name}`, "success");
}

function openRestockModal() {
  const medSelect = document.getElementById("restockMedSelect");
  medSelect.innerHTML = state.medicines.map(m => `
    <option value="${m.id}">${m.name} (Current Stock: ${m.stock})</option>
  `).join("");
  openModal("modalRestock");
}

function handleRestockSubmit(e) {
  e.preventDefault();
  const medId = parseInt(document.getElementById("restockMedSelect").value);
  const qty = parseInt(document.getElementById("restockQty").value);

  const med = state.medicines.find(m => m.id === medId);
  if (!med) return;

  med.stock += qty;
  state.liveFeed.unshift({
    icon: "📦",
    msg: `Restocked ${qty}x ${med.name}. New level: ${med.stock}`,
    time: "Just now",
    type: "info"
  });

  saveState();
  closeModal("modalRestock");
  renderPharmacy();
  renderDashboard();
  showToast(`Restocked ${qty} units of ${med.name}`, "success");
}

// 3.7 DBMS SQL REPORTS
const DBMS_REPORTS = [
  {
    num: 1,
    title: "Doctor Consultations Aggregation",
    sql: `<span class="sql-keyword">SELECT</span> d.doctor_id, d.first_name, d.specialization, 
       <span class="sql-function">COUNT</span>(a.appointment_id) <span class="sql-keyword">AS</span> total_appointments,
       <span class="sql-function">SUM</span>(d.consultation_fee) <span class="sql-keyword">AS</span> total_revenue
<span class="sql-keyword">FROM</span> <span class="sql-table">doctors</span> d
<span class="sql-keyword">LEFT JOIN</span> <span class="sql-table">appointments</span> a <span class="sql-keyword">ON</span> d.doctor_id = a.doctor_id
<span class="sql-keyword">GROUP BY</span> d.doctor_id, d.first_name, d.specialization
<span class="sql-keyword">ORDER BY</span> total_appointments <span class="sql-keyword">DESC</span>;`,
    generateData: () => {
      return state.doctors.map(d => {
        const appts = state.appointments.filter(a => a.doctor === d.name).length;
        return {
          "Doctor ID": `#${d.id}`,
          "Doctor Name": d.name,
          "Specialty": d.dept,
          "Appointments": appts,
          "Consultation Rate": `$${d.fee}`,
          "Est. Revenue Generated": `$${(appts * d.fee).toFixed(2)}`
        };
      });
    }
  },
  {
    num: 2,
    title: "Ward Bed Occupancy & Projected Daily Revenue",
    sql: `<span class="sql-keyword">SELECT</span> ward_name, 
       <span class="sql-function">COUNT</span>(bed_id) <span class="sql-keyword">AS</span> total_beds,
       <span class="sql-function">SUM</span>(<span class="sql-keyword">CASE WHEN</span> is_occupied = TRUE <span class="sql-keyword">THEN</span> 1 <span class="sql-keyword">ELSE</span> 0 <span class="sql-keyword">END</span>) <span class="sql-keyword">AS</span> occupied_beds,
       <span class="sql-function">SUM</span>(<span class="sql-keyword">CASE WHEN</span> is_occupied = TRUE <span class="sql-keyword">THEN</span> daily_charge <span class="sql-keyword">ELSE</span> 0 <span class="sql-keyword">END</span>) <span class="sql-keyword">AS</span> daily_projected_revenue
<span class="sql-keyword">FROM</span> <span class="sql-table">hospital_beds</span>
<span class="sql-keyword">GROUP BY</span> ward_name
<span class="sql-keyword">HAVING</span> total_beds > 0;`,
    generateData: () => {
      const wards = ["ICU", "General Ward A", "General Ward B", "Emergency Ward"];
      return wards.map(w => {
        const beds = state.beds.filter(b => b.ward === w);
        const occ = beds.filter(b => b.occupied);
        const rev = occ.reduce((sum, b) => sum + b.charge, 0);
        return {
          "Ward Designation": w,
          "Capacity": beds.length,
          "Occupied Count": occ.length,
          "Occupancy Rate": `${beds.length ? Math.round((occ.length / beds.length)*100) : 0}%`,
          "Daily Billed Revenue": `$${rev.toFixed(2)}`
        };
      });
    }
  },
  {
    num: 3,
    title: "Critical Emergency Intake with Doctor Subquery",
    sql: `<span class="sql-keyword">SELECT</span> emergency_id, patient_name_temp, triage_priority, condition_description,
       (<span class="sql-keyword">SELECT</span> specialization <span class="sql-keyword">FROM</span> <span class="sql-table">doctors</span> <span class="sql-keyword">WHERE</span> first_name = e.doctor_name) <span class="sql-keyword">AS</span> assigned_specialty
<span class="sql-keyword">FROM</span> <span class="sql-table">emergency_cases</span> e
<span class="sql-keyword">WHERE</span> triage_priority <span class="sql-keyword">IN</span> (<span class="sql-string">'CRITICAL'</span>, <span class="sql-string">'HIGH'</span>)
<span class="sql-keyword">ORDER BY</span> admission_time <span class="sql-keyword">ASC</span>;`,
    generateData: () => {
      return state.emergencyCases.map(c => ({
        "Intake ID": `#${c.id}`,
        "Patient Tag": c.patientName,
        "Triage Priority": c.priority,
        "Clinical Condition": c.condition,
        "Assigned Physician": c.doctor || "Triage Officer",
        "Bed Location": c.bed
      }));
    }
  },
  {
    num: 4,
    title: "Pharmacy Inventory Stock Valuation & Reorder Thresholds",
    sql: `<span class="sql-keyword">SELECT</span> medicine_id, name, category, stock_quantity, unit_price,
       (stock_quantity * unit_price) <span class="sql-keyword">AS</span> total_asset_value,
       <span class="sql-keyword">CASE WHEN</span> stock_quantity <= reorder_level <span class="sql-keyword">THEN</span> <span class="sql-string">'CRITICAL_REORDER'</span> <span class="sql-keyword">ELSE</span> <span class="sql-string">'SUFFICIENT'</span> <span class="sql-keyword">END</span> <span class="sql-keyword">AS</span> replenishment_status
<span class="sql-keyword">FROM</span> <span class="sql-table">medicines</span>
<span class="sql-keyword">ORDER BY</span> total_asset_value <span class="sql-keyword">DESC</span>;`,
    generateData: () => {
      return state.medicines.map(m => {
        const val = m.stock * m.price;
        return {
          "Drug ID": `#${m.id}`,
          "Trade Name": m.name,
          "Therapeutic Class": m.category,
          "Stock On Hand": `${m.stock} units`,
          "Unit Price": `$${m.price.toFixed(2)}`,
          "Total Valuation": `$${val.toFixed(2)}`,
          "Status Flag": m.stock <= m.reorder ? "⚠️ REORDER" : "OK"
        };
      });
    }
  }
];

let activeReportData = [];

function loadDbmsReport(reportNum, btn) {
  const report = DBMS_REPORTS.find(r => r.num === reportNum) || DBMS_REPORTS[0];

  document.querySelectorAll("#tabReports .filter-pill").forEach(p => p.classList.remove("active"));
  if (btn) btn.classList.add("active");

  const sqlBox = document.getElementById("sqlQueryDisplayBox");
  if (sqlBox) sqlBox.innerHTML = report.sql;

  activeReportData = report.generateData();

  const thead = document.getElementById("reportTableHead");
  const tbody = document.getElementById("reportTableBody");

  if (activeReportData.length > 0) {
    const keys = Object.keys(activeReportData[0]);
    thead.innerHTML = `<tr>${keys.map(k => `<th>${k}</th>`).join("")}</tr>`;
    tbody.innerHTML = activeReportData.map(row => `
      <tr>
        ${keys.map(k => `<td>${row[k]}</td>`).join("")}
      </tr>
    `).join("");
  } else {
    thead.innerHTML = "";
    tbody.innerHTML = `<tr><td colspan="5" style="text-align:center; padding: 20px;">No report rows returned.</td></tr>`;
  }
}

function exportReportToCSV() {
  if (!activeReportData.length) return;
  const headers = Object.keys(activeReportData[0]);
  const rows = activeReportData.map(r => headers.map(h => `"${r[h]}"`).join(","));
  const csvContent = [headers.join(","), ...rows].join("\n");
  downloadFile(csvContent, "hospital_analytical_report.csv", "text/csv");
  showToast("Exported query results to CSV", "info");
}

function exportReportToJSON() {
  if (!activeReportData.length) return;
  const jsonContent = JSON.stringify(activeReportData, null, 2);
  downloadFile(jsonContent, "hospital_analytical_report.json", "application/json");
  showToast("Exported query results to JSON", "info");
}

function downloadFile(content, fileName, contentType) {
  const a = document.createElement("a");
  const file = new Blob([content], { type: contentType });
  a.href = URL.createObjectURL(file);
  a.download = fileName;
  a.click();
}

// 3.8 APPOINTMENT MODAL
function openAppointmentModal() {
  const patSelect = document.getElementById("apptPatientSelect");
  patSelect.innerHTML = state.patients.map(p => `<option value="${p.id}">${p.name} (#${p.id})</option>`).join("");

  const docSelect = document.getElementById("apptDoctorSelect");
  docSelect.innerHTML = state.doctors.map(d => `<option value="${d.name}">${d.name} (${d.dept} - $${d.fee})</option>`).join("");

  const todayStr = new Date().toISOString().split("T")[0];
  document.getElementById("apptDateInput").value = todayStr;

  openModal("modalAppointment");
}

function openAppointmentForDoctor(docId) {
  openAppointmentModal();
  const doc = state.doctors.find(d => d.id === docId);
  if (doc) {
    document.getElementById("apptDoctorSelect").value = `${doc.name} (${doc.dept} - $${doc.fee})`;
  }
}

function openAppointmentForPatient(patId) {
  openAppointmentModal();
  document.getElementById("apptPatientSelect").value = patId;
}

function handleAppointmentSubmit(e) {
  e.preventDefault();
  const patId = parseInt(document.getElementById("apptPatientSelect").value);
  const docName = document.getElementById("apptDoctorSelect").value.split(" (")[0];
  const date = document.getElementById("apptDateInput").value;
  const slot = document.getElementById("apptTimeSlot").value;
  const reason = document.getElementById("apptReason").value;

  const patient = state.patients.find(p => p.id === patId);
  if (!patient) return;

  // Double-booking conflict check
  const conflict = state.appointments.find(a => a.doctor === docName && a.date === date && a.slot === slot);
  if (conflict) {
    showToast(`Conflict! ${docName} is already booked at ${slot} on ${date}. Please select another time slot.`, "danger");
    return;
  }

  const newAppt = {
    id: Date.now(),
    patient: patient.name,
    doctor: docName,
    date: date,
    slot: slot,
    reason: reason,
    status: "SCHEDULED"
  };

  state.appointments.push(newAppt);
  state.liveFeed.unshift({
    icon: "📅",
    msg: `Consultation Booked: ${patient.name} with ${docName} (${slot})`,
    time: "Just now",
    type: "info"
  });

  saveState();
  closeModal("modalAppointment");
  renderDashboard();
  showToast(`Consultation slot confirmed with ${docName} at ${slot}`, "success");
}

// Global Quick Search
function handleGlobalSearch(query) {
  const q = query.trim().toLowerCase();
  if (!q) return;

  // Search across patients or drugs
  const matchedPat = state.patients.find(p => p.name.toLowerCase().includes(q) || p.phone.includes(q));
  if (matchedPat) {
    switchTab("patients");
    return;
  }

  const matchedMed = state.medicines.find(m => m.name.toLowerCase().includes(q) || m.category.toLowerCase().includes(q));
  if (matchedMed) {
    switchTab("pharmacy");
    return;
  }

  const matchedDoc = state.doctors.find(d => d.name.toLowerCase().includes(q) || d.dept.toLowerCase().includes(q));
  if (matchedDoc) {
    switchTab("doctors");
  }
}

// Modal Helpers
function openModal(modalId) {
  const m = document.getElementById(modalId);
  if (m) m.classList.add("active");
}

function closeModal(modalId) {
  const m = document.getElementById(modalId);
  if (m) m.classList.remove("active");
}

// Toast Notifications
function showToast(message, type = "info") {
  const container = document.getElementById("toastContainer");
  if (!container) return;

  const toast = document.createElement("div");
  toast.className = `toast toast-${type}`;
  toast.innerHTML = `
    <span>${type === 'success' ? '✔' : type === 'danger' ? '✖' : 'ℹ'}</span>
    <span>${message}</span>
  `;

  container.appendChild(toast);
  setTimeout(() => {
    toast.style.opacity = "0";
    toast.style.transform = "translateX(50px)";
    toast.style.transition = "all 0.3s ease";
    setTimeout(() => toast.remove(), 300);
  }, 4000);
}

// Render All Panels on startup
function renderAllPanels() {
  renderDashboard();
  renderDoctors();
  renderBeds();
  renderEmergency();
  renderPatients();
  renderPharmacy();
  loadDbmsReport(1);
}

// 5-Second Real-Time Poller (simulating live telemetry)
setInterval(() => {
  // Random small vital check / telemetry event
  updateKpiBadges();
}, 5000);

// Initialize on Load
document.addEventListener("DOMContentLoaded", () => {
  renderAllPanels();
});
