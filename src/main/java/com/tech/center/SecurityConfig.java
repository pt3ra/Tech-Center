package com.tech.center;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	UserService userService;
	
	@Bean
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean                                                             
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		
		daoAuthenticationProvider.setPasswordEncoder(bcryptPasswordEncoder());
		daoAuthenticationProvider.setUserDetailsService(this.userService);
		
		return daoAuthenticationProvider;
	}
	
	/*@Bean
	@Order(1)
	public SecurityFilterChain securefilterChain(HttpSecurity http) throws Exception {
	    http
	    	.securityMatcher("/order/create")
	    	.authorizeHttpRequests(authorize -> authorize
	    		.anyRequest().authenticated()
	    	).httpBasic(withDefaults())
	    	.logout(withDefaults())
	    	.authenticationProvider(daoAuthenticationProvider());
	    
	    return http.build();
	}*/
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	    	.csrf(csrf -> csrf.disable())
	    	.authorizeHttpRequests(authorize -> authorize.requestMatchers("/", "register", "/process_register", "/order/list/**", "/order/view/**").permitAll()
	    		//.requestMatchers("/profile/**")
	    		.anyRequest().authenticated()
	    	).formLogin(login -> login
	                .loginPage("/login") 
	                .loginProcessingUrl("/login")
	                .permitAll() 
	                .defaultSuccessUrl("/", true)
	            ).logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	            		.logoutSuccessUrl("/"))
			//.httpBasic(withDefaults())
			.authenticationProvider(daoAuthenticationProvider());
	    
	    return http.build();
	}
	
	/*@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	    	.authorizeHttpRequests(authorize -> authorize
	    		.anyRequest().permitAll()
	    	).authenticationProvider(daoAuthenticationProvider());
	    
	    return http.build();
	}*/
}
