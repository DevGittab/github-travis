package net.gittab.githubtravis.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.gittab.githubtravis.vo.UserVO;

/**
 * ServiceApi.
 *
 * @author xiaohua zhou
 **/
@FeignClient(value = "serviceApi", url = "http://github-travis", path = "internal/api/service")
public interface ServiceApi {

	/**
	 * find user by id.
	 * @param id user id
	 * @return user
	 */
	@GetMapping("findById")
	UserVO findById(@RequestParam("id") Long id);

}
