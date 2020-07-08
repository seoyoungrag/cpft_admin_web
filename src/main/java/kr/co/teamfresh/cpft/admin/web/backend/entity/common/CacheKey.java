package kr.co.teamfresh.cpft.admin.web.backend.entity.common;

public class CacheKey {
	public static final int DEFAULT_EXPIRE_SEC = 60; // 1 minutes
	public static final String USER = "user";
	public static final int USER_EXPIRE_SEC = 60 * 5; // 5 minutes
	public static final String BOARD = "board";
	public static final int BOARD_EXPIRE_SEC = 60 * 10; // 10 minutes
	public static final String POST = "post";
	public static final String POSTS = "posts";
	public static final int POST_EXPIRE_SEC = 60 * 5; // 5 minutes
	public static final String CARRIER = "carrier";
	public static final int CARRIER_EXPIRE_SEC = 60 * 24; // 24 hours
	public static final String CODE = "code";
	public static final int CODE_EXPIRE_SEC = 60 * 24; // 24 hours
}