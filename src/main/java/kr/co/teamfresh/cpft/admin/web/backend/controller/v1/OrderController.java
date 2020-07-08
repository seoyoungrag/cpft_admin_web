package kr.co.teamfresh.cpft.admin.web.backend.controller.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.co.teamfresh.cpft.admin.web.backend.dto.order.OrderDTO;
import kr.co.teamfresh.cpft.admin.web.backend.dto.order.OrderTruckOwnerForApplicationListDTO;
import kr.co.teamfresh.cpft.admin.web.backend.model.response.ListResult;
import kr.co.teamfresh.cpft.admin.web.backend.model.response.PageReqRes;
import kr.co.teamfresh.cpft.admin.web.backend.model.response.SingleResult;
import kr.co.teamfresh.cpft.admin.web.backend.service.ResponseService;
import kr.co.teamfresh.cpft.admin.web.backend.service.order.OrderService;
import kr.co.teamfresh.cpft.admin.web.backend.util.ObjectMapperUtils;
import kr.co.teamfresh.cpft.admin.web.backend.entity.order.Order;
import kr.co.teamfresh.cpft.admin.web.backend.entity.order.OrderTruckOwner;
import kr.co.teamfresh.cpft.admin.web.backend.entity.order.OrderTruckOwner_;
import lombok.RequiredArgsConstructor;

@Api(tags = { "5. Order" })
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/order")
public class OrderController {

	Logger logger = LoggerFactory.getLogger(OrderController.class);
	private final ResponseService responseService;
	private final OrderService orderService;
	@PersistenceContext
	private final EntityManager entityManager;

	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@ApiOperation(value = "오더 저장", notes = "오더를 저장한다.")
	@PostMapping
	public SingleResult<OrderDTO> saveOrder(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody OrderDTO order) {
		return responseService.getSingleResult(orderService.saveOrder(ObjectMapperUtils.map(order, Order.class)));
	}

	@ApiOperation(value = "모든 오더 조회", notes = "사용자 운송사의 모든 오더를 조회한다.")
	@GetMapping("/carrier/{carrierSeq}")
	public ListResult<OrderDTO> listOrderByCarrierId(@RequestHeader("X-AUTH-TOKEN") String token,
			@PathVariable String carrierSeq) {
		return responseService.getListResult(orderService.findAllByCarrierSeq(carrierSeq));
	}
	
	@ApiOperation(value = "특정 오더 조회", notes = "사용자 운송사의 진행상테에 대한 오더를 조회한다.")
	@GetMapping("/carrier/{carrierSeq}/order/status/{status}")
	public ListResult<OrderDTO> listOrderByCarrierIdAndStatus(@RequestHeader("X-AUTH-TOKEN") String token,
			@PathVariable String carrierSeq, @PathVariable String status) {
		return responseService.getListResult(orderService.findAllByCarrierSeqAndStatus(carrierSeq, status));
	}

	@ApiOperation(value = "오더의 지원자(차주) 조회", notes = "오더의 지원자들을 조회한다.")
	@GetMapping("/{orderSeq}/status/{status}/truckOwner")
	public PageReqRes<OrderTruckOwner, OrderTruckOwnerForApplicationListDTO> listOrderTruckOwnerBydOrderSeq(@PathVariable String orderSeq,@PathVariable String status,
			@ApiParam(value = "오더 지원자 페이징 정보", required = true) @ModelAttribute PageReqRes<OrderTruckOwner, OrderTruckOwnerForApplicationListDTO> page) {
		if (orderSeq.equals("all")) {
			return responseService.getPageResult(page, orderService.listOrderTruckOwnerByStatusOrderByCreatedAt(status, page),
					OrderTruckOwnerForApplicationListDTO.class);
		} else {
			return responseService.getPageResult(page,
					orderService.listOrderTruckOwnerByOrderSeqOrderAndStatusByCreatedAt(status, orderSeq, page),
					OrderTruckOwnerForApplicationListDTO.class);
		}
	}

	@ApiOperation(value = "오더 지원의 열람 여부를 수정", notes = "오더의 열람 여부를 수정한다.")
	@PutMapping("/{orderSeq}/truckOwner/{userSeq}/isRead")
	@Transactional
	public SingleResult<Integer> updateOrderTruckOwnerIsRead(@PathVariable String orderSeq,
			@PathVariable String userSeq,
			@ApiParam(value = "오더 지원 열람 여부", required = true) @RequestBody OrderTruckOwnerForApplicationListDTO ordertruckOwnerDTO) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaUpdate<OrderTruckOwner> update = cb.createCriteriaUpdate(OrderTruckOwner.class);
		Root<OrderTruckOwner> orderTruckOwnerRoot = update.from(OrderTruckOwner.class);
		update.set(orderTruckOwnerRoot.get(OrderTruckOwner_.isRead), ordertruckOwnerDTO.getIsRead());
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(cb.equal(orderTruckOwnerRoot.get(OrderTruckOwner_.orderSeq), orderSeq));
		predicates.add(cb.equal(orderTruckOwnerRoot.get(OrderTruckOwner_.userSeq), userSeq));
		update.where(predicates.toArray(new Predicate[] {}));
		return responseService.getSingleResult(entityManager.createQuery(update).executeUpdate());
	}

	@ApiOperation(value = "오더 지원의 진행 상태를 수정", notes = "오더의 진행 상태를 수정한다.")
	@PutMapping("/{orderSeq}/truckOwner/{userSeq}/status")
	@Transactional
	public SingleResult<Integer> updateOrderTruckOwnerStatus(@PathVariable String orderSeq,
			@PathVariable String userSeq,
			@ApiParam(value = "오더 지원의 진행 상태 정보", required = true) @RequestBody OrderTruckOwnerForApplicationListDTO ordertruckOwnerDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaUpdate<OrderTruckOwner> update = cb.createCriteriaUpdate(OrderTruckOwner.class);
		Root<OrderTruckOwner> orderTruckOwnerRoot = update.from(OrderTruckOwner.class);
		update.set(orderTruckOwnerRoot.get(OrderTruckOwner_.status), ordertruckOwnerDTO.getStatus());
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(cb.equal(orderTruckOwnerRoot.get(OrderTruckOwner_.orderSeq), orderSeq));
		predicates.add(cb.equal(orderTruckOwnerRoot.get(OrderTruckOwner_.userSeq), userSeq));
		update.where(predicates.toArray(new Predicate[] {}));
		return responseService.getSingleResult(entityManager.createQuery(update).executeUpdate());
	}
}
