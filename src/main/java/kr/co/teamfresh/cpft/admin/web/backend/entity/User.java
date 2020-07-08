package kr.co.teamfresh.cpft.admin.web.backend.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import kr.co.teamfresh.cpft.admin.web.backend.entity.common.CommonDateEntity;
import kr.co.teamfresh.cpft.admin.web.backend.entity.order.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Builder // builder를 사용할수 있게 합니다.
@Getter // user 필드값의 getter를 자동으로 생성합니다.
@Setter
@NoArgsConstructor // 인자없는 생성자를 자동으로 생성합니다.
@AllArgsConstructor // 인자를 모두 갖춘 생성자를 자동으로 생성합니다.
@Entity // jpa entity임을 알립니다.
@Table(name = "USER") // 'user' 테이블과 매핑됨을 명시
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Post Entity에서 User와의 관계를 Json으로 변환시 오류 방지를 위한 코드
@Proxy(lazy = false)
public class User extends CommonDateEntity implements UserDetails { // 날짜 필드 상속 처리	
	@Id // pk
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="USER_SEQ")
	private String userSeq;
	
	@Column(nullable = false, unique = true, length = 50, name="USER_LOGIN_ID")
	private String userLoginId;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(nullable = false, length = 100, name="USER_LOGIN_PW")
	private String userLoginPw;
	
	@Column(nullable = false, length = 100, name="USER_NM")
	private String userNm;

	@Column(nullable = false, length = 100, name="USER_EMAIL")
	private String userEmail;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<Order> orders = new HashSet<Order>(0);

	@ManyToOne
	@JoinColumn(name = "CARRIER_SEQ", insertable = false, updatable = false)
	@JsonManagedReference
	private Carrier carrier;
	
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
			name="USER_ROLE",
			joinColumns=@JoinColumn(name = "USER_SEQ", referencedColumnName = "USER_SEQ")
	)
    @Column(name="ROLE")
	@Builder.Default
	private List<String> roles = new ArrayList<>();


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rolePK.user")
	private Set<UserRole> userRoles= new HashSet<UserRole>(0);

    @OneToOne
    @PrimaryKeyJoinColumn
    @JoinColumn(name = "USER_SEQ")
    private TruckOwner truckOwner;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public String getUsername() {
		return this.userLoginId;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public String getPassword() {
		return this.userLoginPw;
	}
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isEnabled() {
		return true;
	}
}