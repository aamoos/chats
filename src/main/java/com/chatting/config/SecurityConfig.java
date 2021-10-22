package com.chatting.config;

import com.chatting.common.Constants;
import com.chatting.common.Url;
import com.chatting.service.CustomUsersDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * Security 설정 클래스
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoginSucessHandler loginSucessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final DataSource dataSource;
    private final CustomUsersDetailService customUsersDetailService;

    /* configure */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new FrontAuthenticationProvider());
        super.configure(auth);
    }

    /**
     * http 요청검사
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /* 자동 로그인 설정 */
        http.rememberMe()
                .key("jpa") //쿠키에 사용되는 값을 암호화하기 위한 키(key)값
                .rememberMeParameter("remember-me")
                .userDetailsService(customUsersDetailService)
                .tokenRepository(tokenRepository()) //DataSource 추가
                .tokenValiditySeconds(604800); //토큰 유지 시간(초단위) - 일주일

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/file-download/**").permitAll()            //파일 다운로드
                .antMatchers("/auth/**").permitAll()					    //로그인, 회원가입 접속허용
                .antMatchers("/resource/**/images/**").permitAll()		//이미지
                .anyRequest().authenticated()
                .and()

                //로그인 화면 설정
                .formLogin()
                .permitAll()
                .loginPage(Url.AUTH.LOGIN)
                .loginProcessingUrl(Url.AUTH.LOGIN_PROC)
                .successHandler(loginSucessHandler)
                .failureHandler(loginFailureHandler)
                .usernameParameter(USERNAME_PARAM)
                .passwordParameter(PASSWORD_PARAM)
                .and()
                .logout()
                .logoutUrl(Url.AUTH.LOGOUT_PROC)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .and()

                //세션관리
                .sessionManagement()
                .maximumSessions(200) 				//세션 허용 갯수
                .expiredUrl(Url.AUTH.LOGIN)		 	//세션 만료시 이동할 페이지
                .sessionRegistry(sesionRegistry())
                .maxSessionsPreventsLogin(true);	//동시 로그인 차단, false인 경우 기존 세션 만료

    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        web
                .ignoring()
                .antMatchers(Constants.STATIC_RESOURCES_URL_PATTERS)
                .antMatchers(HttpMethod.GET, "/exception/**");
        super.configure(web);
    }

    /**
     * 패스워드 암호화
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sesionRegistry() {
        return new SpringSecuritySessionRegistImpl();
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        // JDBC 기반의 tokenRepository 구현체
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource); // dataSource 주입
        return jdbcTokenRepository;
    }

    /* 관리자 아이디 파라미터 이름 */
    public static final String USERNAME_PARAM = "userId";

    /* 관리자 비밀번호 파라미터 이름 */
    public static final String PASSWORD_PARAM = "password";
}
