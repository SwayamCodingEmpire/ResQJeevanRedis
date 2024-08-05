package com.spm.resqjeevanredis.interceptors;
import com.spm.resqjeevanredis.service.JwtServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class SessionChannelInterceptor implements ChannelInterceptor {
    private final Logger logger = LoggerFactory.getLogger(SessionChannelInterceptor.class);
    private final JwtServiceImpl jwtService;
    private final UserDetailsService userDetailsService;

    public SessionChannelInterceptor(JwtServiceImpl jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        logger.info("PreSend: {}", message.toString());
        MessageHeaders messageHeaders = message.getHeaders();
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(StompCommand.CONNECT.equals(accessor.getCommand())){
            final String jwtToken = accessor.getFirstNativeHeader("Authorization");
            logger.info("JWT Token: {}", jwtToken);
            try
            {
                String webSocketSessionId = (String) messageHeaders.get("simpSessionId");
                logger.info("WebSocket Session ID: {}", webSocketSessionId);
                final String jwtTokenWithoutBearer = jwtToken.substring(7);
                logger.info("JWT Token without Bearer: {}", jwtTokenWithoutBearer);
                final String username = jwtService.extractUsername(jwtTokenWithoutBearer);
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if(username!=null && authentication==null){
                    logger.info("Setting Security Context");
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    if(jwtService.isTokenValid(jwtTokenWithoutBearer, userDetails)){
                        logger.info("Token is valid");
                       final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        accessor.setUser(authenticationToken);
                        logger.info(authentication.getName());
                    }
                }
            }catch (Exception e){
                logger.error("Error while setting Security Context: {}", e.getMessage());
                return null;
            }
        }
        else if (StompCommand.DISCONNECT.equals(accessor.getCommand()) || StompCommand.UNSUBSCRIBE.equals(accessor.getCommand()) || StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            return message;
        } else {
            final String jwtTokenFull = accessor.getFirstNativeHeader("Authorization");
            logger.info(accessor.getCommand() + "     " + accessor.getFirstNativeHeader("Authorization"));
            try {
                String websocketSessionId = (String) messageHeaders.get("simpSessionId");
                logger.info("websocketSessionId: " + websocketSessionId);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null) {
                    logger.info("authentication is null");
                    final String jwtToken = jwtTokenFull.substring(7);
                    logger.info("jwtToken: " + jwtToken);
                    final String username = jwtService.extractUsername(jwtToken);
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    if (jwtService.isTokenValid(jwtToken, userDetails)) {
                        logger.info("Token is valid");
                        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        accessor.setUser(authenticationToken);
                        logger.info(authenticationToken.getName());
                    }
                }
            } catch (Exception e) {
                logger.error("else is the problem");
                e.printStackTrace();
                return null;
            }

            return message;
        }
        return message;
    }

}