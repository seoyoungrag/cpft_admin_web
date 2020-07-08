package kr.co.teamfresh.cpft.admin.web.backend.repo.code;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.teamfresh.cpft.admin.web.backend.entity.code.CodeCtgry;

public interface CodeCtgryJpaRepo  extends JpaRepository<CodeCtgry, String> {

	List<CodeCtgry> findAllByCodesCodeUseYn(char c);

}
