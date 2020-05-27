package br.com.gouvea.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import br.com.gouvea.job.JobCompletionNotificationListener;
import br.com.gouvea.model.Produto;
import br.com.gouvea.processor.ProdutoItemProcessor;

@Configuration
public class BatchConfiguration {

	@Value("${app.file.reader}")
	private String appFileReader;
	
	private final JobBuilderFactory jobBuilderFactory;

	private final StepBuilderFactory stepBuilderFactory;

	private final DataSource dataSource;

	public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			javax.sql.DataSource dataSource) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.dataSource = dataSource;
	}

	@Bean
	public FlatFileItemReader<Produto> reader() {
		FlatFileItemReader<Produto> reader = new FlatFileItemReader<>();
		reader.setResource(new FileSystemResource(appFileReader));
		reader.setLineMapper(new DefaultLineMapper<Produto>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "nome", "descricao", "quantidade" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Produto>() {
					{
						setTargetType(Produto.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	public ProdutoItemProcessor processor() {
		return new ProdutoItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Produto> writer() {
		JdbcBatchItemWriter<Produto> writer = new JdbcBatchItemWriter<>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		writer.setSql("INSERT INTO produto (nome, descricao, quantidade, created) VALUES (:nome, :descricao,:quantidade, now())");
		writer.setDataSource(this.dataSource);
		return writer;
	}
	
	@Bean
	public Job importProdutoJob(JobCompletionNotificationListener listener) {
		return jobBuilderFactory.get("importProdutoJob").incrementer(new RunIdIncrementer()).listener(listener)
				.flow(step1()).end().build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<Produto, Produto>chunk(10).reader(reader()).processor(processor())
				.writer(writer()).build();
	}

}
