package net.gittab.githubtravis.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ActivityStat.
 *
 * @author xiaohua zhou
 **/
@Data
@Entity
@Accessors(chain = true)
@Table(name = "activity_stats")
public class ActivityStat implements Serializable {

	@Id
	@GeneratedValue
	private Long activityId;

	/**
	 * 浏览量.
	 */
	private Long timesViewed;

	/**
	 * 作品数.
	 */
	private Long worksCount;

	/**
	 * 用户数.
	 */
	private Long userCount;

}
