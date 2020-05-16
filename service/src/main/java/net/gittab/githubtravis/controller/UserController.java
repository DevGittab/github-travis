package net.gittab.githubtravis.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.gittab.githubtravis.domain.User;

/**
 * UserController.
 *
 * @author xiaohua zhou
 **/
@RestController
@RequestMapping("v1/api/user")
public class UserController {

	@GetMapping("list")
	public List<User> list() {
		return Collections.emptyList();
	}

}
