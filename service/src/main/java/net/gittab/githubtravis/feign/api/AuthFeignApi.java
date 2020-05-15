package net.gittab.githubtravis.feign.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * AuthFeignApi.
 *
 * @author xiaohua zhou
 **/
@FeignClient(value = "authApi", url = "http://auth-service")
public interface AuthFeignApi {

	/**
	 * find tenant id by domain.
	 * @param domain domain
	 * @return domain of tenant id
	 */
	@GetMapping("findByDomain")
	Long findByDomain(@RequestParam("domain") String domain);

}
