import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@QuarkusMain
public class MainApiApplication {

    public static void main(String... args) {
        Quarkus.run(args);
        logMemoryInfo();
    }

    private static void logMemoryInfo() {
        Runtime runtime = Runtime.getRuntime();

        final NumberFormat format = NumberFormat.getInstance();

        final long maxMemory = runtime.maxMemory();
        final long allocatedMemory = runtime.totalMemory();
        final long freeMemory = runtime.freeMemory();
        final long mb = 1024 * 1024;
        final String mega = " MB";

        System.out.println("========================== Memory Info ==========================");
        System.out.println("Free memory: " + format.format(freeMemory / mb) + mega);
        System.out.println("Allocated memory: " + format.format(allocatedMemory / mb) + mega);
        System.out.println("Max memory: " + format.format(maxMemory / mb) + mega);
        System.out.println(
                "Total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / mb) + mega);
        System.out.println("=================================================================");
        System.out.println(
                "Started in : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        System.out.println("=================================================================\n");
    }
}