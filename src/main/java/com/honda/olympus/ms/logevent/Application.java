package com.honda.olympus.ms.logevent;

import com.honda.olympus.ms.logevent.config.ApplicationProperties;
import com.honda.olympus.ms.logevent.config.DefaultProfileUtil;
import com.honda.olympus.ms.logevent.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.TimeZone;


@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class Application implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private final Environment env;

    public Application(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);

    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Mexico_City"));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(Constants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(Constants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run with both the 'dev' and 'prod' profiles at the same time.");
        }
    }


    @Bean
    protected MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }


    @Bean
    protected LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }

        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");

        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }

        log.info(
                "\n----------------------------------------------------------\n" +
                "\tApplication is running! Access URLs:\n" +
                "\tLocal: \t\t{}://localhost:{}{}\n" +
                "\tProfile(s): \t{}\n" +
                "----------------------------------------------------------\n",
                protocol,
                serverPort,
                contextPath,
                env.getActiveProfiles());
    }

}
