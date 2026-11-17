# CSA09-192511345-Assignment3

## Student details
- **Name:** S. Jenefar
- **Register number:** 192511345
- **Course:** Programming in Java – Assignment 3 (CO3)
- **Topics:** Exception handling, resource safety, multithreading, synchronization, deadlock prevention, producer–consumer communication and concurrent processing.
- **Java version:** Java 17 or later recommended (uses `Path.of`, `Files.writeString` and modern Java APIs).

## Repository structure

| Folder | Program | Coverage |
|---|---|---|
| `Question1` | `ATMExceptionDemo.java` | ATM input validation, exception hierarchy, multi-catch and finally |
| `Question2` | `StorageDemo.java`, `ExamPortalDemo.java` | User-defined checked exceptions, propagation and exception chaining |
| `Question3` | `SalesReportDemo.java` | Manual resource closing and try-with-resources |
| `Question4` | `ScannerStationDemo.java` | Thread, Runnable, start, sleep, join and isAlive |
| `Question5` | `SeatBooking.java`, `BookingBenchmark.java` | Atomic seat booking and synchronization |
| `Question6` | `TokenCounter.java`, `WalletTransferDemo.java` | wait/notifyAll producer-consumer and deadlock prevention |
| `Question7` | `ResultProcessor.java` | ExecutorService, Callable, Future and concurrent result summary |

## Compile and run

Open a terminal in the repository root. Compile each program from its own folder, then run it:

```bash
javac Question1/ATMExceptionDemo.java
java -cp Question1 ATMExceptionDemo

javac Question2/StorageDemo.java Question2/ExamPortalDemo.java
java -cp Question2 StorageDemo
java -cp Question2 ExamPortalDemo

javac Question3/SalesReportDemo.java
java -cp Question3 SalesReportDemo

javac Question4/ScannerStationDemo.java
java -cp Question4 ScannerStationDemo

javac Question5/SeatBooking.java Question5/BookingBenchmark.java
java -cp Question5 SeatBooking
java -cp Question5 BookingBenchmark

javac Question6/TokenCounter.java Question6/WalletTransferDemo.java
java -cp Question6 TokenCounter
java -cp Question6 WalletTransferDemo

javac Question7/ResultProcessor.java
java -cp Question7 ResultProcessor
```

On Windows PowerShell or Command Prompt, the same commands work with a JDK installed and its `bin` directory on PATH.

## Expected checks

- **Q1:** A valid transaction succeeds only when numeric input is valid, the amount is positive and within the balance, and `card-reader.txt` exists in the working directory. The finally message is printed for all handled outcomes.
- **Q2:** `StorageDemo` prints the expected successful save, storage error and original cause. `ExamPortalDemo` demonstrates validation and storage failures.
- **Q3:** Creates sample files under `input/`, demonstrates a malformed record and prints the report generated with try-with-resources.
- **Q4:** Four stations complete; completion order can vary. The final total is printed after all joins.
- **Q5:** Expected output: `Seats sold: 10`.
- **Q6:** Expected output: `Sum of tokens collected: 210 (expected 210)`. Wallet transfer demo preserves the total balance.
- **Q7:** Generates score files under `scores/`, deliberately includes missing and corrupt records, and reports results for pool sizes 1, 2, 4 and 8. It prints actual elapsed times from the machine on which it is run.

## Evidence to add after local execution

The assignment requires both group members to execute every question individually. After running the programs on your computer:

1. Save terminal output screenshots in the corresponding Question folder.
2. Record your own ten-run thread completion observations for Q4 and Q5.
3. Record Q7 timing output as raw measurements; timings vary by computer and must not be copied from another run.
4. Use your IDE debugger for Q3 and record the observed resource-leak behaviour.
5. Add your test tables, exception stack trace, complexity comparison and individual contribution record to the joint report.

**Important:** No screenshots, debugger observations or benchmark timings are claimed as completed in this repository. They must be captured from your own execution for accurate submission evidence.
