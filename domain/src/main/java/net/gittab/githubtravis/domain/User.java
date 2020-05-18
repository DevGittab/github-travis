package net.gittab.githubtravis.domain;

import java.util.concurrent.atomic.LongAdder;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * User.
 *
 * @author xiaohua zhou
 **/
@Data
@JsonNaming
@Accessors(chain = true)
public class User {

	private Long id;

	private String username;

	private String firstName;

	private String lastName;

	private String avatar;

	private String password;

	@Transient
	private String fullName;

	@Override
	public String toString() {
		/// to confirm
		/*
		 * int daysOfThisYear = LocalDate.now().lengthOfYear();
		 * DateTimeFormatterBuilder.getLocalizedDateTimePattern() ThreadLocalRandom
		 * threadLocalRandom = ThreadLocalRandom.current(); threadLocalRandom.nextInt();
		 */
		LongAdder longAdder = new LongAdder();

		return "User{" + "id=" + this.id + ", username='" + this.username + '\''
				+ ", firstName='" + this.firstName + '\'' + ", lastName='" + this.lastName
				+ '\'' + ", avatar='" + this.avatar + '\'' + ", password='"
				+ this.password + '\'' + ", fullName='" + this.fullName + '\'' + '}';
	}

}
