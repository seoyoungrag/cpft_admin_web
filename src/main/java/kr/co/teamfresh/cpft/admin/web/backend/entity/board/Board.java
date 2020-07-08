package kr.co.teamfresh.cpft.admin.web.backend.entity.board;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kr.co.teamfresh.cpft.admin.web.backend.entity.common.CommonDateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="BOARD")
@Getter
@NoArgsConstructor
public class Board extends CommonDateEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long boardId;
	@Column(nullable = false, length = 100, name="NAME")
	private String name;
}

