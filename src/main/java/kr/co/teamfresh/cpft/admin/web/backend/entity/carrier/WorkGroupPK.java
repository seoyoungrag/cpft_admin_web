package kr.co.teamfresh.cpft.admin.web.backend.entity.carrier;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class WorkGroupPK implements Serializable {
    @Column(name = "CARRIER_SEQ")
	private String carrierSeq;
    @Column(name = "WORK_GROUP_NM")
	private String workGroupNm;
}
