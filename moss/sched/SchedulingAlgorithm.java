import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {

  public static Results Run(int runtime, Vector<sProcess> processVector, Results result) {
    int timeQuantum = 100; // Example time quantum (adjust as needed)
    int comptime = 0;
    int size = processVector.size();
    int completed = 0;
    String resultsFile = "Summary-Processes";

    result.schedulingType = "Batch (Preemptive)"; // Modified to reflect preemptive scheduling
    result.schedulingName = "Round-Robin"; // Updated scheduling algorithm name

    try {
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));

      // Implement round-robin scheduling logic
      while (comptime < runtime) {
        for (int i = 0; i < size; i++) {
          sProcess process = processVector.elementAt(i);

          if (process.cpudone < process.cputime) {
            // Process has remaining CPU time
            if (process.cpuBurst == 0) {
              // Process just arrived or completed I/O
              out.println("Process: " + i + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
            }

            // Execute the process for the time quantum or until completion
            int remainingTime = process.cputime - process.cpudone;
            int executeTime = Math.min(remainingTime, timeQuantum);
            process.cpudone += executeTime;
            process.cpuBurst += executeTime;

            // Check if process completes execution
            if (process.cpudone == process.cputime) {
              out.println("Process: " + i + " completed... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
              completed++;
            } else if (process.cpuBurst >= process.ioblocking) {
              // Process needs to perform I/O
              out.println("Process: " + i + " I/O blocked... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.cpudone + ")");
              process.cpuBurst = 0; // Reset CPU burst for next execution
            }
          }
        }
        comptime += timeQuantum; // Increment simulation time by time quantum
      }

      out.close();
    } catch (IOException e) {
      // Handle exceptions
    }

    result.compuTime = comptime;
    return result;
  }
}
