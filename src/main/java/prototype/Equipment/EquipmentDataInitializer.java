package prototype.Equipment;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(10)
public class EquipmentDataInitializer implements DataInitializer {

	private static final Logger LOG= LoggerFactory.getLogger(EquipmentDataInitializer.class);

	private EquipmentRepository equipmentRepository;

	EquipmentDataInitializer(EquipmentRepository equipmentRepository){
		this.equipmentRepository = equipmentRepository;
		Equipment equipment1 = new Equipment("stage1", Equipment.EquipmentType.STAGE);
		equipmentRepository.save(equipment1);
	}

	@Override
	public void initialize() {

		if (equipmentRepository.findAll().iterator().hasNext()){
			return;
		}

		//LOG.info("Create default catalog entries");

		//equipmentRepository.save(new Equipment("stage1", Equipment.EquipmentType.STAGE));
	}
}
