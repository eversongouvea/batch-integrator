package br.com.gouvea.job;

import java.io.File;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	@Value("${app.file.reader}")
	private String appFileReader;
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		 log.info("INICIANDO JOB");
	}
	
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
        	new File(appFileReader).delete();
            log.info("FINALIZADO JOB -> STATUS {}",BatchStatus.COMPLETED);
        }
    }
	
}
