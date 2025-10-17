# 🛫 WorldWanderer Unit Testing (Assignment 4)

## 📘 Overview

This repository contains the implementation and unit testing of the `FlightSearch` class for the WorldWanderer travel website project.

The task is part of **Assignment 4 (Individual)** for the course ISYS3413/ISYS3475/ISYS1118 – Software Engineering Fundamentals at RMIT University.

The goal of this activity is to implement and test the `runFlightSearch()` function, which validates flight search conditions such as passenger limits, dates, airport codes, and seating restrictions.

***

## 🚀 How to Run the Program

### 1. Prerequisites
* **Java JDK 17** or higher
* **JUnit 5**
* Any Java IDE (Eclipse / IntelliJ IDEA / VS Code)

### 2. Steps
1.  Clone the repository:
2.  Open the project in your IDE.
3.  Ensure **JUnit 5** is configured in your classpath.
4.  Run `FlightSearchTest.java` as a **JUnit Test**.
5.  All **12 test cases** (and the valid case) should pass successfully.

***
 
###  🧠 Key Validation Rules:
* Total passengers between **1–9**.
* Children cannot be seated in **emergency rows** or **first class**.
* Infants cannot be seated in **emergency rows** or **business class**.
* Each adult can supervise up to **2 children**.
* Each adult can carry only **1 infant**.
* Departure date cannot be in the **past**.
* Dates must follow **DD/MM/YYYY** format (strict validation).
* Return date must be **after** departure date.
* Seating class must be one of: `economy`, `business`, `first`.
* Only **economy class** can have emergency row seating.
* Valid airport codes: `syd`, `mel`, `lax`, `cdg`, `del`, `pvg`, `doh` (**departure ≠ destination**).

***

## 🧪 Testing Summary

| Detail | Value |
| :--- | :--- |
| **Testing Framework** | JUnit 5 |
| **Total Test Cases** | 12 |
| **Testing Approach** | Boundary Value Testing applied for each condition |
| **Test Results** | All tests passed successfully ✅ |

## 🧾 Author Details

**Name:** Abilash Lathaharan  
**Student ID:** s4139458  
**Course:** Software Engineering Fundamentals (ISYS3413 / ISYS3475 / ISYS1118)
