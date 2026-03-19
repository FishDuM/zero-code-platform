package hk.ljx.zerocodeplatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("hk.ljx.zerocodeplatform.mapper")
public class ZeroCodePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroCodePlatformApplication.class, args);
    }

}
