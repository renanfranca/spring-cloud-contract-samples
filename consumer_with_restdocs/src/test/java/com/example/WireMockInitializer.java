package com.example;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.SocketUtils;

import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;

public class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private String[] servicesUrlProperties = { "beer-api-producer-restdocs.url" };

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		int randomPort = SocketUtils.findAvailableTcpPort(5000, 5100);

		applicationContext.getBeanFactory().registerSingleton("Options", setupWireMockConfigurationOption(randomPort));

		setupServicesUrl(randomPort, applicationContext);
	}

	private com.github.tomakehurst.wiremock.core.Options setupWireMockConfigurationOption(int port) {
		com.github.tomakehurst.wiremock.core.WireMockConfiguration factory = WireMockSpring.options();
		factory.port(port);
		factory.notifier(new Slf4jNotifier(true));
		factory.extensions(new ResponseTemplateTransformer(false));
		return factory;
	}

	private void setupServicesUrl(int port, ConfigurableApplicationContext applicationContext) {
		String baseUrl = "http://localhost:" + port;
		for (String serviceUrl : servicesUrlProperties) {
			TestPropertyValues.of(serviceUrl + "=" + baseUrl).applyTo(applicationContext);
		}
	}
}
