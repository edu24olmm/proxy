package lee.study.proxyee.server;

import com.google.common.base.Joiner;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lee.study.proxyee.crt.CertUtil;
import lee.study.proxyee.exception.HttpProxyExceptionHandle;
import lee.study.proxyee.handler.HttpProxyServerHandle;
import lee.study.proxyee.intercept.CertDownIntercept;
import lee.study.proxyee.intercept.HttpProxyIntercept;
import lee.study.proxyee.intercept.HttpProxyInterceptInitializer;
import lee.study.proxyee.intercept.HttpProxyInterceptPipeline;
import lee.study.proxyee.proxy.ProxyConfig;
import lee.study.proxyee.proxy.ProxyType;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.KeyPair;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class HttpProxyServer {

    //http代理隧道握手成功
    public final static HttpResponseStatus SUCCESS = new HttpResponseStatus(200,
            "Connection established");

    private HttpProxyServerConfig serverConfig;
    private HttpProxyInterceptInitializer proxyInterceptInitializer;
    private HttpProxyExceptionHandle httpProxyExceptionHandle;
    private ProxyConfig proxyConfig;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    // 定义申请获得的appKey和appSecret
    static String appkey = "139739624";
    static String secret = "b62f39d059b54c2480ec5b3d4ec0a2ba";
    final static String proxyIP = "forward.xdaili.cn";
    final static int proxyPort = 80;

    public HttpProxyServer() {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SslContext getClientSslContext() {
        return serverConfig.getClientSslCtx();
    }

    private void init() throws Exception {
        //注册BouncyCastleProvider加密库
        Security.addProvider(new BouncyCastleProvider());
        if (serverConfig == null) {
            serverConfig = new HttpProxyServerConfig();
            serverConfig.setClientSslCtx(
                    SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
                            .build());
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            X509Certificate certificate = CertUtil.loadCert(classLoader.getResourceAsStream("ca.crt"));
            //读取CA证书使用者信息
            serverConfig.setIssuer(CertUtil.getSubject(certificate));
            //读取CA证书有效时段(server证书有效期超出CA证书的，在手机上会提示证书不安全)
            serverConfig.setCaNotBefore(certificate.getNotBefore());
            serverConfig.setCaNotAfter(certificate.getNotAfter());
            //CA私钥用于给动态生成的网站SSL证书签证
            serverConfig
                    .setCaPriKey(CertUtil.loadPriKey(classLoader.getResourceAsStream("ca_private.der")));
            //生产一对随机公私钥用于网站SSL证书动态创建
            KeyPair keyPair = CertUtil.genKeyPair();
            serverConfig.setServerPriKey(keyPair.getPrivate());
            serverConfig.setServerPubKey(keyPair.getPublic());
            serverConfig.setLoopGroup(new NioEventLoopGroup());
        }
        if (proxyInterceptInitializer == null) {
            proxyInterceptInitializer = new HttpProxyInterceptInitializer();
        }
        if (httpProxyExceptionHandle == null) {
            httpProxyExceptionHandle = new HttpProxyExceptionHandle();
        }
    }

    public HttpProxyServer serverConfig(HttpProxyServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        return this;
    }

    public HttpProxyServer proxyInterceptInitializer(
            HttpProxyInterceptInitializer proxyInterceptInitializer) {
        this.proxyInterceptInitializer = proxyInterceptInitializer;
        return this;
    }

    public HttpProxyServer httpProxyExceptionHandle(
            HttpProxyExceptionHandle httpProxyExceptionHandle) {
        this.httpProxyExceptionHandle = httpProxyExceptionHandle;
        return this;
    }

    public HttpProxyServer proxyConfig(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
        return this;
    }

    public void start(int port) {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
//          .option(ChannelOption.SO_BACKLOG, 100)
//          .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<Channel>() {

                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast("httpCodec", new HttpServerCodec());
                            ch.pipeline().addLast("serverHandle",
                                    new HttpProxyServerHandle(serverConfig, proxyInterceptInitializer, proxyConfig,
                                            httpProxyExceptionHandle));
                        }
                    });
            ChannelFuture f = b
                    .bind(port)
                    .sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public static String authHeader(String orderno, String secret, int timestamp) {
        //拼装签名字符串
        String planText = String.format("orderno=%s,secret=%s,timestamp=%d", orderno, secret, timestamp);

        //计算签名
        String sign = org.apache.commons.codec.digest.DigestUtils.md5Hex(planText).toUpperCase();

        //拼装请求头Proxy-Authorization的值
        String authHeader = String.format("sign=%s&orderno=%s&timestamp=%d", sign, orderno, timestamp);
        System.out.println(authHeader);
        return authHeader;
    }

    public static String getAuthHeader() {

        // 创建参数表
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("app_key", appkey);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));// 使用中国时间，以免时区不同导致认证错误
        paramMap.put("timestamp", format.format(new Date()));

        // 对参数名进行排序
        String[] keyArray = paramMap.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);

        // 拼接有序的参数名-值串
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(secret);
        for (String key : keyArray) {
            stringBuilder.append(key).append(paramMap.get(key));
        }

        stringBuilder.append(secret);
        String codes = stringBuilder.toString();

        // MD5编码并转为大写， 这里使用的是Apache codec
        String sign = org.apache.commons.codec.digest.DigestUtils.md5Hex(codes).toUpperCase();

        paramMap.put("sign", sign);

        // 拼装请求头Proxy-Authorization的值，这里使用 guava 进行map的拼接
        String authHeader = "MYH-AUTH-MD5 " + Joiner.on('&').withKeyValueSeparator("=").join(paramMap);

        System.out.println(authHeader);

        return authHeader;
    }

    public static String getAuthHeader2() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));// 使用中国时间，以免时区不同导致认证错误

        String code = "orderno=ZF20181258247M7dXuk,secret=b62f39d059b54c2480ec5b3d4ec0a2ba,timestamp=" + format.format(new Date());
        String sign = org.apache.commons.codec.digest.DigestUtils.md5Hex(code).toUpperCase();
        System.out.println(sign);

        return sign;


    }

    public static void main(String[] args) throws Exception {
//        new HttpProxyServer().start(9999);

        new HttpProxyServer()
                .proxyConfig(new ProxyConfig(ProxyType.HTTP, proxyIP, proxyPort))  //使用socks5二级代理
//                .proxyConfig(new ProxyConfig(ProxyType.HTTP, "1.194.122.8", 27961))  //使用socks5二级代理
                .proxyInterceptInitializer(new HttpProxyInterceptInitializer() {
                    @Override
                    public void init(HttpProxyInterceptPipeline pipeline) {
                        pipeline.addLast(new CertDownIntercept());  //处理证书下载
                        pipeline.addLast(new HttpProxyIntercept() {
                            @Override
                            public void beforeRequest(Channel clientChannel, HttpRequest httpRequest,
                                                      HttpProxyInterceptPipeline pipeline) throws Exception {
                                //替换UA，伪装成手机浏览器
                                httpRequest.headers().set(HttpHeaderNames.USER_AGENT,
                                        "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
                                //转到下一个拦截器处理
                                int timestamp = (int) (new Date().getTime() / 1000);
                                final String authHeader = authHeader("ZF20181258247M7dXuk", "b62f39d059b54c2480ec5b3d4ec0a2ba", timestamp);
//                                httpRequest.headers().set("Proxy-Authorization", authHeader);
                                pipeline.beforeRequest(clientChannel, httpRequest);
                            }

                            @Override
                            public void afterResponse(Channel clientChannel, Channel proxyChannel,
                                                      HttpResponse httpResponse, HttpProxyInterceptPipeline pipeline) throws Exception {

                                //拦截响应，添加一个响应头
                                httpResponse.headers().add("intercept", "test");
                                //转到下一个拦截器处理
                                pipeline.afterResponse(clientChannel, proxyChannel, httpResponse);
                            }
                        });
                    }
                })
                .httpProxyExceptionHandle(new HttpProxyExceptionHandle() {
                    @Override
                    public void beforeCatch(Channel clientChannel, Throwable cause) throws Exception {
                        System.out.println("111111111111111");
                        cause.printStackTrace();
                    }

                    @Override
                    public void afterCatch(Channel clientChannel, Channel proxyChannel, Throwable cause) throws Exception {
                        System.out.println("22222222222222");
                        cause.printStackTrace();
                    }
                })
                .start(9999);
    }

}
