package kr.co.teamfresh.cpft.admin.web.backend.entity.order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import kr.co.teamfresh.cpft.admin.web.backend.entity.Carrier;
import kr.co.teamfresh.cpft.admin.web.backend.entity.User;
import kr.co.teamfresh.cpft.admin.web.backend.entity.carrier.WorkGroupManager;
import kr.co.teamfresh.cpft.admin.web.backend.entity.common.CommonDateEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // 인자를 모두 갖춘 생성자를 자동으로 생성합니다.
@Table(name = "ORDER")
public class Order extends CommonDateEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ORDER_SEQ")
	private String orderSeq;
	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "WORK_GROUP_NM", referencedColumnName = "WORK_GROUP_NM", nullable = false)
	@JoinColumn(name = "CARRIER_SEQ", referencedColumnName = "CARRIER_SEQ", nullable = false)
	@JoinColumn(name = "WORK_GROUP_MANAGER_NM", referencedColumnName = "WORK_GROUP_MANAGER_NM", nullable = false)
	private WorkGroupManager workGroupManager;
	@Column(nullable = false, length = 200, name="RCRIT_TYPE")
	private String rcritType;
	@Column(nullable = false, length = 200, name="RCRIT_MANS")
	private String rcritMans;
	
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@CollectionTable(
			name="ORDER_CAR_TYPE",
			joinColumns=@JoinColumn(name = "ORDER_SEQ", referencedColumnName = "ORDER_SEQ")
	)
    @Column(name="CAR_TYPE")
	@Builder.Default
	private List<String> carTypes = new ArrayList<>();
	
	@Column(nullable = false, length = 200, name="TON_TYPE")
	private String tonType;
	@Column(nullable = false, length = 200, name="DLVY_PRDLST")
	private String dlvyPrdlst;
	@Column(nullable = false, length = 200, name="PAY_AMT")
	private String payAmt;
	@Column(nullable = false, length = 200, name="PAY_FULL_TYPE")
	private String payFullType;
	@Column(nullable = false, length = 200, name="WORKING_AREA")
	private String workingArea;
	@Column(nullable = true, length = 200, name="WORKING_AREA_ETC")
	private String workingAreaEtc;
	@Column(nullable = false, length = 200, name="OPRAT_SCTN")
	private String opratSctn;
	@Column(nullable = false, length = 200, name="WORKING_DAYS_TYPE")
	private String workingDaysType;

	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@CollectionTable(
			name="ORDER_WORK_DAY",
			joinColumns=@JoinColumn(name = "ORDER_SEQ", referencedColumnName = "ORDER_SEQ")
	)
    @Column(name="WORK_DAY")
	@Builder.Default
	private List<String> workDays = new ArrayList<>();
	
	@Column(nullable = false, length = 100, name="WORK_HOUR_START")
	private int workHourStart;
	@Column(nullable = false, length = 100, name="WORK_MINUTE_START")
	private int workMinuteStart;
	@Column(nullable = false, length = 100, name="WORK_HOUR_END")
	private int workHourEnd;
	@Column(nullable = false, length = 100, name="WORK_MINUTE_END")
	private int workMinuteEnd;
	@Column(nullable = false, length = 1000, name="DETAIL_MATTER")
	private String detailMatter;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_SEQ", nullable = false)
	@JsonProperty(access = Access.READ_ONLY)
	private User user; // 오더 - 회원의 관계 - N:1
	@Column(nullable = false, length = 100, name="STATUS")
	private String status;
	@ManyToOne
	@JoinColumn(name = "CARRIER_SEQ", insertable = false, updatable = false, nullable = false)
	@JsonManagedReference
	private Carrier carrier;
	//@OneToMany(fetch = FetchType.LAZY, mappedBy = "orderTruckOwnerPK.order")
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name="ORDER_SEQ")
	private Set<OrderTruckOwner> orderTruckOwners = new HashSet<OrderTruckOwner>(0);

}
