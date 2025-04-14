package com.yutsuki.chatserver.websocket;

import com.yutsuki.chatserver.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class AuthWebsocketInterceptor implements HandshakeInterceptor {

    private final TokenService tokenService;
    /*
    example url connect
    ws://localhost:8080/ws?Authorization=<access_token>
     */
    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {
        boolean result = false;
        log.trace("beforeHandshake...");

        var queryParams = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();
        var authorization = queryParams.getFirst(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authorization)) {
            log.debug("authorization => {}", authorization);
            try {
                var user = tokenService.getUserByToken(authorization);
                attributes.put("userId", user.getId());

                result = true;
            } catch (Exception e) {
                log.warn("BeforeHandshake-[block]. error: {}", e.getMessage());
            }
        }
        return result;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
        log.trace("afterHandshake...");
    }
}
