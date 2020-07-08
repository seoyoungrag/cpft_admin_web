package kr.co.teamfresh.cpft.admin.web.backend.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import kr.co.teamfresh.cpft.admin.web.backend.entity.carrier.WorkGroup;
import kr.co.teamfresh.cpft.admin.web.backend.entity.common.CommonDateEntity;
import kr.co.teamfresh.cpft.admin.web.backend.entity.order.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CARRIER")
@Getter
@Setter
@NoArgsConstructor
public class Carrier extends CommonDateEntity  implements Serializable { 
	@Id // pk
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="CARRIER_SEQ")
	private String carrierSeq;

	@Column(nullable = false, length = 100, name="CARRIER_NM")
	private String CarrierNm;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "carrier")
	@JsonBackReference
	private Set<User> users = new HashSet<User>(0);
	
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "carrier")
	private Set<WorkGroup> workGroups = new HashSet<WorkGroup>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	@JsonIgnore
	private Set<Order> orders = new HashSet<Order>(0);
}

		
	
