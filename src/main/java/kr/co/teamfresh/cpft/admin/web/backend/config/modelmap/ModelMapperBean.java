package kr.co.teamfresh.cpft.admin.web.backend.config.modelmap;

import org.hibernate.collection.internal.PersistentSet;
import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.co.teamfresh.cpft.admin.web.backend.config.modelmap.converter.OrderDtoToOrderEnCv;

@Configuration
public class ModelMapperBean {
	
    @Bean
    public ModelMapper modelMapper() {
    	ModelMapper m = new ModelMapper();
    	m.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT)
    	.setPropertyCondition(new Condition<Object, Object>() {
  	      public boolean applies(MappingContext<Object, Object> context) {
  	    	  if(context.getSource() instanceof PersistentSet) {
  	    		  try {
  	    			  return !((PersistentSet) context.getSource()).isEmpty();
  	    		  }catch(Exception e) {
  	    			  //영속성 전이가 이루어져 객체가 존재할 경우에만 매핑을 시도한다. PersistentSet이 프록시로 동작하기에 프록시 오브젝트의 에러 타입을 잡아낼 수 가 없다.
  	    			  return false;
  	    		  }
  	    	  }else {
  	    		  return true;
  	    	  }
  	    	  //return !(context.getSource() instanceof PersistentCollection);
  	      }
    	});
    	//m.addMappings(new OrderDtoToEnMap());
    	m.addConverter(new OrderDtoToOrderEnCv());
        return m;
    }


}