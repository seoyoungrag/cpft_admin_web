package kr.co.teamfresh.cpft.admin.web.backend.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Embeddable
public class UserRolePK implements Serializable {
	@ManyToOne
	@JoinColumn(name = "USER_SEQ", updatable = false, insertable = false)
	private User user;
    @Column(name = "ROLE", updatable = false, insertable = false)
	private String role;
}
