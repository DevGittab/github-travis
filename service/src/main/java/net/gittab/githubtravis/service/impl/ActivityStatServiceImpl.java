package net.gittab.githubtravis.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import net.gittab.githubtravis.domain.ActivityStat;
import net.gittab.githubtravis.service.ActivityStatService;

/**
 * ActivityStatServiceImpl.
 *
 * @author xiaohua zhou
 **/
@Slf4j
@Service
public class ActivityStatServiceImpl implements ActivityStatService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public ActivityStat findById(Long id) {
		String sql = "select * from activity_stats where activity_id = ?";
		return this.jdbcTemplate.queryForObject(sql, new Object[] { id }, (rs, i) -> {
			ActivityStat activityStat = new ActivityStat();
			activityStat.setActivityId(rs.getLong("activity_id"));
			activityStat.setTimesViewed(rs.getLong("times_viewed"));
			activityStat.setWorksCount(rs.getLong("works_count"));
			activityStat.setUserCount(rs.getLong("user_count"));
			return activityStat;
		});
	}

	@Override
	public Long findTimesViewedById(Long id) {
		String sql = "select times_viewed from activity_stats where activity_id = ?";
		return this.jdbcTemplate.queryForObject(sql, Long.class, id);
	}

	@Override
	public List<ActivityStat> findByWorksCount(Long worksCount) {
		String sql = "select * from activity_stats where works_count = ?";
		return this.jdbcTemplate.queryForList(sql, ActivityStat.class, worksCount);
	}

	@Override
	public List<Long> findIdByWorksCount(Long worksCount) {
		String sql = "select activity_id from activity_stats where works_count = ?";
		return this.jdbcTemplate.queryForList(sql, Long.class, worksCount);
	}

	@Override
	public void insertStats(List<ActivityStat> activityStatList) {
		if (CollectionUtils.isEmpty(activityStatList)) {
			return;
		}
		String sql = "insert into activity_stats(activity_id, times_viewed, works_count, user_count) values(?, ?, ?, ?)";
		long start = System.currentTimeMillis();
		activityStatList.forEach(item -> {
			this.jdbcTemplate.update(sql, preparedStatement -> {
				preparedStatement.setLong(1, item.getActivityId());
				preparedStatement.setLong(2, item.getTimesViewed());
				preparedStatement.setLong(3, item.getWorksCount());
				preparedStatement.setLong(4, item.getUserCount());
			});
		});
		log.info("insert state cost {} s", (System.currentTimeMillis() - start) / 1000);
	}

	@Override
	public void batchInsertStats(List<ActivityStat> activityStatList) {
		if (CollectionUtils.isEmpty(activityStatList)) {
			return;
		}
		String sql = "insert into activity_stats(activity_id, times_viewed, works_count, user_count) values(?, ?, ?, ?)";
		long start = System.currentTimeMillis();
		this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int i)
					throws SQLException {
				ActivityStat data = activityStatList.get(i);
				preparedStatement.setLong(1, data.getActivityId());
				preparedStatement.setLong(2, data.getTimesViewed());
				preparedStatement.setLong(3, data.getWorksCount());
				preparedStatement.setLong(4, data.getUserCount());
			}

			@Override
			public int getBatchSize() {
				return activityStatList.size();
			}
		});
		log.info("batch insert state cost {} s",
				(System.currentTimeMillis() - start) / 1000);
	}

}
