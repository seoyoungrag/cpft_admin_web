package kr.co.teamfresh.cpft.admin.web.backend.entity.order;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
@Entity
@Table(name = "ORDER_WORK_DAY")
@Getter
@Setter
@NoArgsConstructor
*/
public class OrderWorkDay implements Serializable {
/*
	@EmbeddedId
	private OrderWorkDayPK orderWorkDayPK;

	@MapsId("orderSeq")
	@ManyToOne
	//@JoinColumn(name = "ORDER_SEQ", insertable = false, updatable = false)
	@JsonBackReference
	private Order order;
	*/
}
