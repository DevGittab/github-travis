package net.gittab.githubtravis.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * User.
 *
 * @author xiaohua zhou
 **/
@Data
@Accessors(chain = true)
public class User {

	private Long id;

	private String username;

}
