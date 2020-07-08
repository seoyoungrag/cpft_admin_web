package kr.co.teamfresh.cpft.admin.web.backend.entity.carrier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import kr.co.teamfresh.cpft.admin.web.backend.entity.Carrier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "WORK_GROUP")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor 
public class WorkGroup implements Serializable {
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "carrierSeq", column = @Column(name = "CARRIER_SEQ", nullable = false)),
			@AttributeOverride(name = "workGroupNm", column = @Column(name = "WORK_GROUP_NM", nullable = false)) })
	private WorkGroupPK workGroupPk;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumns({
	    @JoinColumn(name="CARRIER_SEQ", referencedColumnName="CARRIER_SEQ"),
	    @JoinColumn(name="WORK_GROUP_NM", referencedColumnName="WORK_GROUP_NM")
	})
	private List<WorkGroupManager> workGroupManagers = new ArrayList<WorkGroupManager>();

	@ManyToOne
	@JoinColumn(name = "CARRIER_SEQ", nullable = false, insertable = false, updatable = false)
	@JsonBackReference
	private Carrier carrier;
}
