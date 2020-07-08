package kr.co.teamfresh.cpft.admin.web.backend.entity.code;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="CODE_CTGRY")
@Getter
@NoArgsConstructor
public class CodeCtgry implements java.io.Serializable {

	@Id
	@Column(name = "CODE_CTGRY_NM", unique = true, nullable = false, length = 200)
	private String codeCtgryNm;

	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "codeCtgry")
	private Set<Code> codes = new HashSet<Code>(0);

}
