package net.gittab.githubtravis.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * UserVO.
 *
 * @author xiaohua zhou
 **/
@Data
@Accessors(chain = true)
public class UserVO {

	private Long id;

	private String username;

}
