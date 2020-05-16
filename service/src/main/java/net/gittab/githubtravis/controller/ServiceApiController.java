package net.gittab.githubtravis.controller;

import org.springframework.web.bind.annotation.RestController;

import net.gittab.githubtravis.api.ServiceApi;
import net.gittab.githubtravis.vo.UserVO;

/**
 * ServiceApiController.
 *
 * @author xiaohua zhou
 **/
@RestController
public class ServiceApiController implements ServiceApi {

	@Override
	public UserVO findById(Long id) {
		UserVO userVO = new UserVO();
		userVO.setId(1L).setUsername("custom");
		return userVO;
	}

}
