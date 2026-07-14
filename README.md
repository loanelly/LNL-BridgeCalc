<p align="center">
  <img src="logo.svg" width="150" height="150" alt="LNL-BridgeCalc Logo">
</p>
<h1 align="center">LNL-BridgeCalc</h1>

<p align="center">
  <strong>Advanced Structural Engineering Complex & Dynamic Vector Physics Engine</strong>
  <br>
  <em>Developed for LNL-Engineering IT & Construction Automation Cluster</em>
</p>

<p align="center">
<a href="https://github.com/loanelly/EntropyShield/blob/main/README.md"><img src="https://github.com/loanelly/EntropyShield/blob/main/img/Flags/EN1.png" height="21" align="center"/></a> <a href="https://github.com/loanelly/EntropyShield/blob/main/README_RU.md"><img src="https://github.com/loanelly/EntropyShield/blob/main/img/Flags/result.png" height="26" align="center"/></a>
</p>

---

## 📖 Overview

**LNL-BridgeCalc** is a user-oriented desktop software designed to automate structural integrity checks, calculate limit states, and project active force vectors in real-time. Built specifically to secure infrastructure and prevent catastrophic structural collapses, this application replaces blind estimates with absolute physical equations.

> 🌐 **Looking for localized manuals?** 
> * Comprehensive English Guide: [`documentation_EN.txt`](documentation_EN.txt)
> * Полное руководство на русском языке: [`documentation_RU.txt`](documentation_RU.txt)

---

## 🚀 Key Features

*   **5-Topology Infrastructure Core**: Custom strain and triogonometric equations for **Beam**, **Arch**, **Cable-Stayed**, **Suspension**, and **Rigid Frame** bridges.
*   **Dynamic Variable Form Factor**: The UI adapts dynamically, generating unique parameter input fields specified for the selected bridge architecture.
*   **Interactive Vector Physics Canvas**: Renders responsive bridge schematics superimposing active stress-load vector force paths ($H_{thrust}$, $T_{max}$, $R_1, R_2$, $N_{col}$) on the fly.
*   **Safety Index Evaluation ($K_{safe}$)**: Instantly computes structural margins, warning users with explicit disaster statements if the structure is prone to collapse.
*   **Automated Disk Export**: Generates timestamped and fully formatted structural log sheets (`.txt`) locally upon each calculation cycle.

---

## 🏗️ Technical Architecture & Parameter Profiles

| Bridge Type | Key Input Parameters | Monitored Output Forces & Metrics | Vector Blueprint Labels |
| :--- | :--- | :--- | :--- |
| **Beam Bridge** | Width ($b$), Height ($h$), Live Load ($q_{live}$), $k_{dyn}$ | Bending stress ($\sigma_{act}$), Max deflection ($f_{max}$) | $P_{live} + G_{self}$, Reactions $R_1, R_2$ |
| **Arch Bridge** | Vault rise ($H$), Vault thickness ($t$), Deck $b \times h$ | Foundation horizontal thrust ($H_{thrust}$), Compression ($N_{max}$) | Center mass $G$, Thrust vectors $H_{thrust}$, Support $V$ |
| **Cable-Stayed**| Pylon height ($H$), Cable count, Deck $b \times h$ | Stay tension ($T_{max}$), Vertical tower load ($P_{pylon}$) | Cable strain $T_{max}$, Main tower force $P_{pylon}$ |
| **Suspension** | Cable sag ($f$), Wire diameter ($d$), Deck $b \times h$ | Main rope tension ($T_{support}$), Anchor load ($H_{anchor}$) | Catenary strain $T_{support}$, Anchor pulling $H_{anchor}$ |
| **Rigid Frame** | Column thickness ($t$), Column height ($H$), Deck $b \times h$| Rigid connection moment ($M_{support}$), Axial stress ($\sigma_{comb}$) | Pillar axial force $N_{col}$, Joint moment curve $M_{joint}$ |

---

## 📊 Safety Standards ($K_{safe}$)

The software evaluates material stress against the Ultimate Tensile Strength ($\sigma_B$) using the limit state verification method:

$$\text{Safety Factor } (K_{safe}) = \frac{\text{Ultimate Strength } (\sigma_B)}{\text{Active Mechanical Stress } (\sigma_{act})}$$

*   🟢 **$K_{safe} \ge 1.5$** &rarr; `APPROVED`: Structure is completely stable under peak dynamic layout.
*   🟡 **$1.0 \le K_{safe} < 1.5$** &rarr; `CRITICAL HAZARD`: Plastic strain initiated. Structural safety compromised.
*   🔴 **$K_{safe} < 1.0$** &rarr; `COLLAPSE FAILURE`: Immediate structural stress fracture. The bridge will collapse.

---

## 📦 Build & Installation Guide

### Prerequisites
*   Java JDK 17 or higher
*   Apache Maven 3.8+

### Compile Independent Fat JAR
Build a monolithic, cross-platform standalone executable with pre-bundled JavaFX modules:
```bash
mvn clean package
```

### Run Application
Execute the compiled binary out of the target folder or run it straight from your Desktop:
```bash
java -jar target/LNL-BridgeCalc-1.0-SNAPSHOT.jar
```

---
<p align="center">
  Created with 🛠️ precision by <a href="https://github.com">loanelly</a> for the <strong>LNL-Engineering Cluster</strong>.
</p>
