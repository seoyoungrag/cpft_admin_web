package kr.co.teamfresh.cpft.admin.web.backend.config.modelmap.mapper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.PropertyMap;

import kr.co.teamfresh.cpft.admin.web.backend.dto.carrier.WorkGroupDTO;
import kr.co.teamfresh.cpft.admin.web.backend.dto.carrier.WorkGroupManagerDTO;
import kr.co.teamfresh.cpft.admin.web.backend.entity.carrier.WorkGroup;
import kr.co.teamfresh.cpft.admin.web.backend.entity.carrier.WorkGroupManager;

public class WorkGroupEnToDtoMap extends PropertyMap<WorkGroup, WorkGroupDTO> {
	@Override
	protected void configure() {
		map().setCarrierSeq(source.getWorkGroupPk().getCarrierSeq());
		map().setWorkGroupNm(source.getWorkGroupPk().getWorkGroupNm());
		using(ctx -> generateWorkGroupManagerList( ((WorkGroup)ctx.getSource()).getWorkGroupManagers() )).map(source, destination.getWorkGroupManagers());
	}

	private List<WorkGroupManagerDTO> generateWorkGroupManagerList(List<WorkGroupManager> workGroupManagers) {
		List<WorkGroupManagerDTO> rtnList = new ArrayList<WorkGroupManagerDTO>();
		for(WorkGroupManager manager : workGroupManagers) {
			WorkGroupManagerDTO dto = new WorkGroupManagerDTO();
			dto.setWorkGroupManagerNm(manager.getWorkGroupManagerPK().getWorkGroupManagerNm());
			rtnList.add(dto);
		}
		return rtnList;
	}
}