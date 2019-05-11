package xf.mvc.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;



public class AuthenticatorTest {

    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    @Before
    public void addUser(){
        simpleAccountRealm.addAccount("fanger","love","admin","user");
    }

    @Test
    public void testAuthenticator(){
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(simpleAccountRealm);


        // 主体提交 认证请求
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject= SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("fanger","love");
        subject.login(token);

        System.out.println("isAuthenticated:" + subject.isAuthenticated());

        subject.logout();
    }
}
