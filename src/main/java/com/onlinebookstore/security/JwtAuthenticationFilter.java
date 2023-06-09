package com.onlinebookstore.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Даний клас є фільтром автентифікації, який перевіряє наявність токена JWT у заголовку запиту. Якщо токен відсутній або містить
 * помилки, то запит блокується і повертається помилка. Якщо ж токен присутній та є дійсним, то користувача автентифікується із
 * використанням JWT та отримує доступ до захищених ресурсів.
 * <p>
 * Зокрема, клас JwtAuthenticationFilter містить наступні методи:
 * - doFilterInternal: основний метод, який перевіряє наявність токена JWT у заголовку запиту та автентифікує користувача в разі,
 * якщо токен є дійсним.
 * Також, в класі є наступні поля:
 * - jwtTokenService: сервіс для генерації та перевірки JWT токенів;
 * - userDetailsService: сервіс, який використовується для завантаження даних користувача за його ідентифікатором.
 */

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String token = request.getHeader("Authorization");
        final String jwt;
        final String email;

        String path = request.getRequestURI();

        if (isTokenRequired(path)) {
            if (token != null && token.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            jwt = token != null ? token.substring(7) : null;

            email = jwtTokenService.extactEmail(jwt);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (jwtTokenService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationFilter =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    usernamePasswordAuthenticationFilter.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationFilter);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isTokenRequired(String path) {
        List<String> tokenRequiredPaths = Arrays.asList(
                "/authors/add", "/authors/delete", "/authors/delete/{id}", "/authors/update/**",
                "/books/add", "/books/delete", "/books/delete/{id}", "/books/update", "/books/update/**",
                "/collection/add", "/collection/delete", "/collection/delete/{id}", "/collection/update/**", "/collection/books/**",
                "/order/delete/{id}",
                "/discounts/**",
                "/user/get_users",
                "/order/add", "/order/user_order", "/order/user_history", "/order/confirm", "/order/items/**",
                "/user/cabinet", "/user_wishlist/**",
                "/reviews/add", "/reviews/update/{id}", "/reviews/delete/{id}"
        );

        return tokenRequiredPaths.contains(path);
    }
}
