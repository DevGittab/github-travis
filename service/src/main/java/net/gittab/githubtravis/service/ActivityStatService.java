package net.gittab.githubtravis.service;

import java.util.List;

import net.gittab.githubtravis.domain.ActivityStat;

/**
 * ActivityStatService.
 *
 * @author xiaohua zhou
 **/
public interface ActivityStatService {

	ActivityStat findById(Long id);

	Long findTimesViewedById(Long id);

	List<ActivityStat> findByWorksCount(Long worksCount);

	List<Long> findIdByWorksCount(Long worksCount);

	void insertStats(List<ActivityStat> activityStatList);

	void batchInsertStats(List<ActivityStat> activityStatList);

}
