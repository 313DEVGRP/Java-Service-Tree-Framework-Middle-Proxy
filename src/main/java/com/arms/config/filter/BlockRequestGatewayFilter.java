package com.arms.config.filter;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BlockRequestGatewayFilter extends AbstractGatewayFilterFactory<BlockRequestGatewayFilter.Config> {

	public BlockRequestGatewayFilter() {
		super(Config.class);
	}

	private static final List<String> PERMIT_URL_ARRAY = List.of(
			"/v2/api-docs",
			"/swagger-resources",
			"/v3/api-docs",
			"/swagger-ui"
	);

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			String path = exchange.getRequest().getURI().getPath();
			boolean isPermitted = false;

			for (String permitUrl : PERMIT_URL_ARRAY) {
				if (path.contains(permitUrl)) {
					isPermitted = true;
					break;
				}
			}
			if (!isPermitted) {
				return exchange.getResponse().setComplete();
			}
			return chain.filter(exchange);
		};
	}

	public static class Config {
		private Config() {
		}
	}

	@Override
	public String name() {
		return "BlockRequestGatewayFilter";
	}


}
