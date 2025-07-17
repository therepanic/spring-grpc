/*
 * Copyright 2024-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.grpc.autoconfigure.server.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.autoconfigure.server.GrpcServerExecutorProvider;
import org.springframework.grpc.autoconfigure.server.GrpcServerFactoryAutoConfiguration;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.grpc.server.security.SecurityContextServerInterceptor;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;
import org.springframework.security.web.SecurityFilterChain;

import io.grpc.internal.GrpcUtil;

@ConditionalOnBean(SecurityFilterChain.class)
@Conditional(GrpcServerFactoryAutoConfiguration.OnGrpcServletCondition.class)
@Configuration(proxyBeanMethods = false)
public class GrpcServletSecurityConfigurerAutoConfiguration {

	@Bean
	@GlobalServerInterceptor
	public SecurityContextServerInterceptor securityContextInterceptor() {
		return new SecurityContextServerInterceptor();
	}

	@Bean
	@ConditionalOnMissingBean(GrpcServerExecutorProvider.class)
	public GrpcServerExecutorProvider grpcServerExecutorProvider() {
		return () -> new DelegatingSecurityContextExecutor(GrpcUtil.SHARED_CHANNEL_EXECUTOR.create());
	}

}
