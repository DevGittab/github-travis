package net.gittab.githubtravis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import net.gittab.githubtravis.ApplicationTests;
import net.gittab.githubtravis.domain.ActivityStat;
import org.junit.Test;

/**
 * ActivityStatServiceTest.
 *
 * @author xiaohua zhou
 **/
public class ActivityStatServiceTest extends ApplicationTests {

	@Autowired
	private ActivityStatService activityStatService;

	@Test
	public void testFindById() {
		Long id = 1L;
		ActivityStat activityStat = this.activityStatService.findById(id);
		assert activityStat.getActivityId().equals(1L);
	}

	@Test
	public void testFindTimesViewed() {
		Long id = 2L;
		Long timesViewed = this.activityStatService.findTimesViewedById(id);
		assert timesViewed.equals(500L);
	}

	@Test
	public void testFindByWorksCount() {
		Long worksCount = 200L;
		List<ActivityStat> activityStatList = this.activityStatService
				.findByWorksCount(worksCount);
		assert activityStatList.size() == 2;
	}

	@Test
	public void testFindIdByWorksCount() {
		Long worksCount = 200L;
		List<Long> activityStatList = this.activityStatService
				.findIdByWorksCount(worksCount);
		assert activityStatList.size() == 2;
	}

	private List<ActivityStat> mockStatsData() {
		List<ActivityStat> activityStatList = new ArrayList<>(10000);
		for (int i = 0; i < 10000; i++) {
			ActivityStat activityStat = new ActivityStat();
			activityStat.setActivityId((long) i).setTimesViewed((long) (i + 100))
					.setWorksCount((long) i + 50).setUserCount((long) i + 10);
			activityStatList.add(activityStat);
		}
		return activityStatList;
	}

	@Test
	public void testInsertStats() {
		List<ActivityStat> activityStatList = mockStatsData();
		this.activityStatService.insertStats(activityStatList);
	}

	@Test
	public void testBatchInsertStats() {
		List<ActivityStat> activityStatList = mockStatsData();
		this.activityStatService.batchInsertStats(activityStatList);
	}

}
