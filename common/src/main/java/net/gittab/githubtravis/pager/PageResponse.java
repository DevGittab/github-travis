package net.gittab.githubtravis.pager;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * PageResponse.
 *
 * @param <T> object
 * @author xiaohua zhou
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class PageResponse<T> {

	private int currentPage;

	private int totalPages;

	private long totalItems;

	private List<T> content;

}
