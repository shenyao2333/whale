package com.whale.oauth2.handler;


import com.whale.provider.basices.web.GrabException;
import com.whale.provider.basices.web.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;


/**
 * @author sy
 * @date Created in 2020.10.5 16:30
 * @description oauth自定义的错误处理
 */
@Slf4j
public class ExceptionTranslator implements WebResponseExceptionTranslator {


    @Override
    public ResponseEntity translate(Exception e) throws Exception {
        if (e instanceof InvalidGrantException){
            return ResponseEntity.ok(R.fail(3001,e.getMessage()));
        }else if (e instanceof UsernameNotFoundException) {
            return ResponseEntity.ok(R.fail(3002,e.getMessage()));
        }else if (e instanceof UnsupportedGrantTypeException) {
            return ResponseEntity.ok(R.fail(3003,"不支持该认证方式！"));
        }else if (e instanceof InvalidScopeException){
            return ResponseEntity.ok(R.fail(3004,"授权范围错误"));
        }else if (e instanceof InternalAuthenticationServiceException){
            return ResponseEntity.ok(R.fail(3001,"账号不存在！"));
        }else if (e instanceof InvalidTokenException){
            return ResponseEntity.ok(R.fail(3004,"token失效！"));
        }else if (e instanceof GrabException) {
            return ResponseEntity.ok(R.fail(3004,((GrabException) e).getMsg()));
        }
        return ResponseEntity.ok(R.fail(2003,"登陆错误!"));
    }





}
