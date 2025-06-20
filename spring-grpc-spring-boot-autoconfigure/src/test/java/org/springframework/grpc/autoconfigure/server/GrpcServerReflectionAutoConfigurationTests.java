package org.springframework.grpc.autoconfigure.server;

import io.grpc.BindableService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.grpc.server.lifecycle.GrpcServerLifecycle;

import static org.assertj.core.api.Assertions.assertThat;

public class GrpcServerReflectionAutoConfigurationTests {

	private ApplicationContextRunner contextRunner() {
		return new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(GrpcServerReflectionAutoConfiguration.class))
			.withBean("noopServerLifecylcle", GrpcServerLifecycle.class, Mockito::mock)
			.withBean(BindableService.class, Mockito::mock);
	}

	@Test
	void whenNoBindableServiceDefinedDoesNotAutoConfigureBean() {
		new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(GrpcServerReflectionAutoConfiguration.class))
			.run((context) -> assertThat(context).doesNotHaveBean(GrpcServerReflectionAutoConfiguration.class));
	}

	@Test
	void whenReflectionEnabledFlagNotPresentThenCreateDefaultBean() {
		this.contextRunner().run((context -> assertThat(context).hasBean("serverReflection")));
	}

	@Test
	void whenReflectionEnabledThenCreateBean() {
		this.contextRunner()
			.withPropertyValues("spring.grpc.server.reflection.enabled=true")
			.run((context) -> assertThat(context).hasSingleBean(GrpcServerReflectionAutoConfiguration.class));
	}

	@Test
	void whenReflectionDisabledThenSkipBeanCreation() {
		this.contextRunner()
			.withPropertyValues("spring.grpc.server.reflection.enabled=false")
			.run((context) -> assertThat(context).doesNotHaveBean(GrpcServerReflectionAutoConfiguration.class));
	}

	@Test
	void whenServerEnabledPropertySetFalseThenAutoConfigurationIsSkipped() {
		this.contextRunner()
			.withPropertyValues("spring.grpc.server.enabled=false")
			.run((context) -> assertThat(context).doesNotHaveBean(GrpcServerReflectionAutoConfiguration.class));
	}

	@Test
	void whenServerEnabledPropertyNotSetThenAutoConfigurationIsNotSkipped() {
		this.contextRunner()
			.run((context) -> assertThat(context).hasSingleBean(GrpcServerReflectionAutoConfiguration.class));
	}

	@Test
	void whenServerEnabledPropertySetTrueThenAutoConfigurationIsNotSkipped() {
		this.contextRunner()
			.withPropertyValues("spring.grpc.server.enabled=true")
			.run((context) -> assertThat(context).hasSingleBean(GrpcServerReflectionAutoConfiguration.class));
	}

}
