package io.springbatch.springbatchlecture.lecture.section3_domain.jobrepository;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;

import javax.sql.DataSource;

@RequiredArgsConstructor
//@Configuration
public class CustomBatchConfigurer extends DefaultBatchConfiguration {

    private final DataSource dataSource;


//    @Override
//    public JobRepository jobRepository() throws BatchConfigurationException {
//        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
//        factory.setDataSource(dataSource);
//        factory.setTransactionManager(getTransactionManager());
//        factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
//        factory.setTablePrefix("SYSTEM_");
//
//        try {
//            return factory.getObject();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}
