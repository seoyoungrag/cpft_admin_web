package kr.co.teamfresh.cpft.admin.web.backend.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import kr.co.teamfresh.cpft.admin.web.backend.entity.order.OrderTruckOwner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
@Entity
@Table(name = "TRUCK_OWNER") 
public class TruckOwner {

	@Id @Column(name="USER_SEQ") String userSeq;
	
    @MapsId
    @OneToOne(mappedBy = "truckOwner")
    @JoinColumn(name="USER_SEQ")
    private User user;

    @Column(name="AGE")
    private int age;
    
    @Column(name="CARRER_CN")
    private String carrerCn;
    
    @Column(name="CARRER_DETAIL")
    private String carrerDetail;
    
    @Column(name="PHONE")
    private String phone;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "truckPK.truckOnwer")
	private Set<Truck> trucks = new HashSet<Truck>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "orderTruckOwnerPK.truckOnwer")
	private Set<OrderTruckOwner> orders = new HashSet<OrderTruckOwner>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "orderTruckOwnerPK.truckOnwer")
    @Where(clause = "status = '0802'")
	private Set<OrderTruckOwner> ordersComplete = new HashSet<OrderTruckOwner>(0);
	
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(
			name="TRUCK_OWNER_CRQFC",
			joinColumns=@JoinColumn(name = "USER_SEQ", referencedColumnName = "USER_SEQ")
	)
    @Column(name="CRQFC")
	@Builder.Default
	private List<String> crqfcs = new ArrayList<>();
}
