package com.server.demo.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import com.server.demo.services.MessageService;
import com.server.demo.services.RoutineService;

import lombok.Data;

@Component
@EnableScheduling
@Data
public class DynamicScheduler implements SchedulingConfigurer {

    private final RoutineService routineService;
    private final TaskScheduler taskScheduler;
    private ScheduledTaskRegistrar registerTask;

    @Autowired
    private MessageService messageService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar registerTask) {
        this.registerTask = registerTask;
        updateJobs();
    }

    public void updateJobs() {
        if (registerTask != null) {
            registerTask.destroy();
            registerTask.setScheduler(taskScheduler);
        }
        routineService.getAllRoutines().forEach(routine -> {
            Runnable task = () -> messageService.sendMessage(routine.getMessageId());
            registerTask.addCronTask(new CronTask(task, routine.getCron()));
        });
        if (registerTask != null) {
            registerTask.afterPropertiesSet();
        }

    }
}
