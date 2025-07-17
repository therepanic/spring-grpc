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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.grpc.server.security.AuthenticationProcessInterceptor;
import org.springframework.grpc.server.security.GrpcSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;

// Copied from Spring Boot (https://github.com/spring-projects/spring-boot/issues/43978), except the
// @Bean of type AuthenticationProcessInterceptor
@Configuration(proxyBeanMethods = false)
public class OAuth2ResourceServerOpaqueTokenConfiguration {

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnMissingBean(OpaqueTokenIntrospector.class)
	static class OpaqueTokenIntrospectionClientConfiguration {

		@Bean
		@ConditionalOnProperty(name = "spring.security.oauth2.resourceserver.opaquetoken.introspection-uri")
		SpringOpaqueTokenIntrospector blockingOpaqueTokenIntrospector(OAuth2ResourceServerProperties properties) {
			OAuth2ResourceServerProperties.Opaquetoken opaqueToken = properties.getOpaquetoken();
			return new SpringOpaqueTokenIntrospector(opaqueToken.getIntrospectionUri(), opaqueToken.getClientId(),
					opaqueToken.getClientSecret());
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnMissingBean(AuthenticationProcessInterceptor.class)
	static class OAuth2SecurityFilterChainConfiguration {

		@Bean
		@ConditionalOnBean(OpaqueTokenIntrospector.class)
		@GlobalServerInterceptor
		AuthenticationProcessInterceptor opaqueTokenAuthenticationProcessInterceptor(GrpcSecurity http)
				throws Exception {
			http.authorizeRequests((requests) -> requests.allRequests().authenticated());
			http.oauth2ResourceServer((resourceServer) -> resourceServer.opaqueToken(Customizer.withDefaults()));
			return http.build();
		}

	}

}
