package app.core.scheduler;


import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;

import javax.enterprise.context.ApplicationScoped;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;


@ApplicationScoped
public class Scheduler {

    private final AtomicInteger counter = new AtomicInteger();

    public int getCounter() {
        return counter.get();
    }

    @Scheduled(every = "10s")
    void incrementEach10s() {
        counter.incrementAndGet();
    }

    @Scheduled(cron = "0 0 8 * * ?")
    void cronJobTime(ScheduledExecution execution) {
        counter.incrementAndGet();
        System.out.println(execution.getScheduledFireTime() + " Cron Job executing all 8am");
    }

    @Scheduled(cron = "{counter.cron.expression}")
    void cronJobConfig() {
        LocalDateTime data = LocalDateTime.now();
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String stringDate = DateFor.format(data);

        counter.incrementAndGet();
        System.out.println(stringDate + " - Scheduler - Cron Job Config Completed");
    }
}