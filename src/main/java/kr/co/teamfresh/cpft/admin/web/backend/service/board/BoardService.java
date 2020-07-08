package kr.co.teamfresh.cpft.admin.web.backend.service.board;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import kr.co.teamfresh.cpft.admin.web.backend.advice.exception.CNotOwnerException;
import kr.co.teamfresh.cpft.admin.web.backend.advice.exception.CResourceNotExistException;
import kr.co.teamfresh.cpft.admin.web.backend.advice.exception.CUserNotFoundException;
import kr.co.teamfresh.cpft.admin.web.backend.entity.board.Board;
import kr.co.teamfresh.cpft.admin.web.backend.entity.board.Post;
import kr.co.teamfresh.cpft.admin.web.backend.model.ParamsPost;
import kr.co.teamfresh.cpft.admin.web.backend.repo.UserJpaRepo;
import kr.co.teamfresh.cpft.admin.web.backend.repo.board.BoardJpaRepo;
import kr.co.teamfresh.cpft.admin.web.backend.repo.board.PostJpaRepo;
import kr.co.teamfresh.cpft.admin.web.backend.service.cache.CacheService;
import kr.co.teamfresh.cpft.admin.web.backend.entity.User;
import kr.co.teamfresh.cpft.admin.web.backend.entity.common.CacheKey;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

	private final BoardJpaRepo boardJpaRepo;
	private final PostJpaRepo postJpaRepo;
	private final UserJpaRepo userJpaRepo;
    private final CacheService cacheService;

// 게시판 이름으로 게시판을 조회. 없을경우 CResourceNotExistException 처리
	@Cacheable(value = CacheKey.BOARD, key = "#boardName", unless = "#result == null")
	public Board findBoard(String boardName) {
		return Optional.ofNullable(boardJpaRepo.findByName(boardName)).orElseThrow(CResourceNotExistException::new);
	}

// 게시판 이름으로 게시물 리스트 조회.
	@Cacheable(value = CacheKey.POSTS, key = "#boardName", unless = "#result == null")
	public List<Post> findPosts(String boardName) {
		return postJpaRepo.findByBoard(findBoard(boardName));
	}

// 게시물ID로 게시물 단건 조회. 없을경우 CResourceNotExistException 처리
	public Post getPost(long postId) {
		return postJpaRepo.findById(postId).orElseThrow(CResourceNotExistException::new);
	}

// 게시물을 등록합니다. 게시물의 회원UID가 조회되지 않으면 CUserNotFoundException 처리합니다.
	@CacheEvict(value = CacheKey.POSTS, key = "#boardName")
	public Post writePost(String uid, String boardName, ParamsPost paramsPost) {
		Board board = findBoard(boardName);
		Post post = new Post(userJpaRepo.findByUserLoginId(uid).orElseThrow(CUserNotFoundException::new), board,
				paramsPost.getAuthor(), paramsPost.getTitle(), paramsPost.getContent());
		return postJpaRepo.save(post);
	}

// 게시물을 수정합니다. 게시물 등록자와 로그인 회원정보가 틀리면 CNotOwnerException 처리합니다.
	//@CachePut(value = CacheKey.POST, key = "#post.postId")
	public Post updatePost(long postId, String uid, ParamsPost paramsPost) {
		Post post = getPost(postId);
		User user = post.getUser();
		if (!uid.equals(user.getUserLoginId()))
			throw new CNotOwnerException();
		post.setUpdate(paramsPost.getAuthor(), paramsPost.getTitle(), paramsPost.getContent());
		cacheService.deleteBoardCache(post.getPostId(), post.getBoard().getName());
		return post;
	}

// 게시물을 삭제합니다. 게시물 등록자와 로그인 회원정보가 틀리면 CNotOwnerException 처리합니다.
	public boolean deletePost(long postId, String uid) {
		Post post = getPost(postId);
		User user = post.getUser();
		if (!uid.equals(user.getUserLoginId()))
			throw new CNotOwnerException();
		postJpaRepo.delete(post);
		cacheService.deleteBoardCache(post.getPostId(), post.getBoard().getName());
		return true;
	}
}