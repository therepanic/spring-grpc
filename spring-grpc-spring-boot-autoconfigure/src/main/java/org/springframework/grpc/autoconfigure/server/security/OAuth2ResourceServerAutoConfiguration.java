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

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.grpc.autoconfigure.server.GrpcServerFactoryAutoConfiguration;
import org.springframework.grpc.autoconfigure.server.GrpcServerFactoryAutoConfiguration.GrpcServletConfiguration;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;

import io.grpc.BindableService;

// Copied from Spring Boot (https://github.com/spring-projects/spring-boot/issues/43978)
@AutoConfiguration(before = { GrpcSecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class },
		after = { GrpcServerFactoryAutoConfiguration.class,
				org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration.class })
@EnableConfigurationProperties(OAuth2ResourceServerProperties.class)
@ConditionalOnClass({ BearerTokenAuthenticationToken.class, ObjectPostProcessor.class })
@ConditionalOnMissingBean(GrpcServletConfiguration.class)
@ConditionalOnBean(BindableService.class)
@Import({ OAuth2ResourceServerConfiguration.JwtConfiguration.class,
		OAuth2ResourceServerConfiguration.OpaqueTokenConfiguration.class })
class OAuth2ResourceServerAutoConfiguration {

}
