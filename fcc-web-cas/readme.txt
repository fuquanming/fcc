cas客户端接入
1、如果cas登录成功后，
    1）该用户名存在该系统，使用本系统权限
    2）该用户名不存在该系统，新建该用户，用户类型为casUser，默认使用用户类型为casUser所绑定的权限
    3）如果需要扩展登录后的用户权限，实现com.fcc.web.sys.service.CasServer接口，类命名为CasServer，如：
        package com.fcc.web.sys.service.impl;
        @Service
        public class CasService implements com.fcc.web.sys.service.CasService {
            @Resource
            private com.fcc.web.sys.service.impl.CasServer casServiceImpl;// 系统默认的casService实现
            @Override
            public SysUser login(Assertion assertion) throws RefusedException, Exception {
                // 获取系统实现的SysUser对象
                SysUser sysUser = casServiceImpl.login(assertion);
                // TODO 修改sysUser.getRoles()对象，更新用户权限
            }
            
        }
    4）系统重写了org.jasig.cas.client.authentication.AuthenticationFilter，应用超时交给系统本身处理，cas不拦截
        
2、maven，cas依赖
    <dependency>
        <groupId>org.jasig.cas.client</groupId>
        <artifactId>cas-client-core</artifactId>
    </dependency>
3、web.xml新增如下filter
    <!-- cas begin -->
    <filter>  
        <filter-name>SsoSession</filter-name>  
        <filter-class>org.jasig.cas.client.session.SingleSignOutFilter</filter-class>  
    </filter>  
    <filter>  
        <filter-name>CAS Authentication Filter</filter-name>  
        <filter-class>org.jasig.cas.client.authentication.AuthenticationFilter</filter-class>  
        <init-param>  
            <param-name>casServerLoginUrl</param-name>  
            <param-value>https://cas.fqm.com:8443/cas</param-value>  
        </init-param>  
        <init-param>  
            <param-name>serverName</param-name>  
            <param-value>http://localhost:10000</param-value>  
        </init-param>
        <init-param>  
            <param-name>ignorePattern</param-name>  
            <param-value>http://localhost:10000/demoSSn01/login.jsp|/static/css/|/static/js/|http://localhost:1010/demoSSn01/$|http://localhost:1010/demoSSn01/login$|http://localhost:1010/demoSSn01/logout$</param-value>  
        </init-param>  
    </filter>  
    <filter>  
        <filter-name>CAS Validation Filter</filter-name>  
        <filter-class>org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter</filter-class>  
        <init-param>  
            <param-name>casServerUrlPrefix</param-name>  
            <param-value>https://cas.fqm.com:8443/cas</param-value>  
        </init-param>  
        <init-param>  
            <param-name>serverName</param-name>  
            <param-value>http://localhost:10000</param-value>  
        </init-param>  
    </filter>  
    <filter>  
        <filter-name>CAS HttpServletRequest Wrapper Filter</filter-name>  
        <filter-class>org.jasig.cas.client.util.HttpServletRequestWrapperFilter</filter-class>  
    </filter>  
    <filter>  
        <filter-name>CAS Assertion Thread Local Filter</filter-name>  
        <filter-class>org.jasig.cas.client.util.AssertionThreadLocalFilter</filter-class>  
    </filter>  
    <filter-mapping>  
        <filter-name>SsoSession</filter-name>  
        <url-pattern>/cas/login.do</url-pattern>  
    </filter-mapping>
    <filter-mapping>  
        <filter-name>CAS Authentication Filter</filter-name>  
        <url-pattern>/cas/login.do</url-pattern>  
    </filter-mapping>
    <filter-mapping>  
        <filter-name>CAS Validation Filter</filter-name>  
        <url-pattern>/cas/login.do</url-pattern>  
    </filter-mapping>
    <filter-mapping>  
        <filter-name>CAS Assertion Thread Local Filter</filter-name>  
        <url-pattern>/cas/login.do</url-pattern>  
    </filter-mapping>
    <!-- cas end -->
5、login.jsp 添加 cas或平常登录代码
String casUrl = (String) request.getServletContext().getAttribute("CAS_URL");
if (casUrl != null && !casUrl.equals("")) {
    // 是否server登录
    String login = request.getParameter("login");
    if (!"normal".equals(login)) {
        // 使用cas登录
        String path_ = request.getContextPath();
        String sPort_ = (request.getServerPort() == 80)?"":(":" + request.getServerPort());
        String base_ = request.getScheme()+"://"+request.getServerName()+sPort_;
        //String base = request.getScheme()+"://"+request.getServerName();
        String basePath_ = base_ + path_ + "/";
        response.sendRedirect(casUrl + "/login?service=" + basePath_ + "cas/login.do");
    }
}
%>    

6、base.jsp 新增 casUrl的js变量及cas登录地址
var casUrl = '${CAS_URL}';
if (casUrl != '') {
    loginUrl = casUrl + "/login?service=${basePath}cas/login.do"
}

7、manage/index.jsp 新增判断退出系统是否跳转到cas退出地址
function logout(b) {
    var goPage = loginUrl;
    if (casUrl != '') {
        goPage = casUrl + "/logout?service=${basePath}";
    }
    $.get('logout.do', function() {
        location.href = goPage;
    });
}
    