package net.gittab.githubtravis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

import net.gittab.githubtravis.feign.api.AuthFeignApi;

/**
 * Application.
 *
 * @author xiaohua zhou
 */
@ServletComponentScan
@EnableFeignClients(basePackageClasses = AuthFeignApi.class)
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
