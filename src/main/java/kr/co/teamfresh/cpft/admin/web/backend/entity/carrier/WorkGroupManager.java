package kr.co.teamfresh.cpft.admin.web.backend.entity.carrier;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import kr.co.teamfresh.cpft.admin.web.backend.entity.order.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "WORK_GROUP_MANAGER")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkGroupManager implements Serializable {

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "carrierSeq", column = @Column(name = "CARRIER_SEQ", nullable = false)),
			@AttributeOverride(name = "workGroupNm", column = @Column(name = "WORK_GROUP_NM", nullable = false)),
			@AttributeOverride(name = "workGroupManagerNm", column = @Column(name = "WORK_GROUP_MANAGER_NM", nullable = false)) })
	private WorkGroupManagerPK workGroupManagerPK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "CARRIER_SEQ", insertable = false, updatable = false),
			@JoinColumn(name = "WORK_GROUP_NM", insertable = false, updatable = false) })
	private WorkGroup workGroup;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workGroupManager")
	@PrimaryKeyJoinColumn
	@JsonManagedReference
	private Set<Order> orders = new HashSet<Order>(0);
}
