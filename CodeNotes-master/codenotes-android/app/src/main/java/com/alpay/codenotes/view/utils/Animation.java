package com.alpay.codenotes.view.utils;

import com.alpay.codenotes.view.GestureImageView;

public interface Animation {
	
	/**
	 * Transforms the view.
	 * @param view
	 * @param diffTime
	 * @return true if this animation should remain active.  False otherwise.
	 */
	public boolean update(GestureImageView view, long time);
	
}
