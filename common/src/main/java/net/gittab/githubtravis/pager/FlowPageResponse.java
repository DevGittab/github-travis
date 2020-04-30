package net.gittab.githubtravis.pager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * FlowPageResponse.
 *
 * @param <T> object
 * @author xiaohua zhou
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FlowPageResponse<T> {

	private long size;

	private long totalElements;

	private T content;

	public static <T> FlowPageResponse<T> empty(long size) {
		return new FlowPageResponse<>(size, 0, null);
	}

}
