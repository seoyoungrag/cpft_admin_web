package kr.co.teamfresh.cpft.admin.web.backend.repo.board;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.teamfresh.cpft.admin.web.backend.entity.board.Board;
import kr.co.teamfresh.cpft.admin.web.backend.entity.board.Post;

public interface PostJpaRepo extends JpaRepository<Post, Long> {
	List<Post> findByBoard(Board board);
}