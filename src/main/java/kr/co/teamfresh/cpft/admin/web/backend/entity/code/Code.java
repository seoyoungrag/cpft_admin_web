package kr.co.teamfresh.cpft.admin.web.backend.entity.code;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import kr.co.teamfresh.cpft.admin.web.backend.entity.common.CommonDateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="CODE")
@Getter
@NoArgsConstructor
public class Code extends CommonDateEntity implements Serializable {

	@Id
	@Column(name = "CODE", unique = true, nullable = false, length = 8)
	private String code;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODE_CTGRY_NM", nullable = false)

	private CodeCtgry codeCtgry;

	@Column(name = "CODE_CTGRY_NM", nullable = false, insertable = false, updatable = false)

	private String codeCtgryNm;

	@Column(name = "CODE_VALUE", nullable = false, length = 200)

	private String codeValue;

	@Column(name = "CODE_DC", length = 200)

	private String codeDc;

	@Column(name = "CODE_USE_YN", nullable = false, length = 1)

	private char codeUseYn;
}
