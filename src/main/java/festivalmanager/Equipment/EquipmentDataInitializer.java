package festivalmanager.Equipment;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import festivalmanager.Equipment.Equipment.EquipmentType;

import static org.salespointframework.core.Currencies.EURO;

@Component
@Order(10)
public class EquipmentDataInitializer implements DataInitializer {

	private static final Logger LOG= LoggerFactory.getLogger(EquipmentDataInitializer.class);

	private EquipmentRepository equipmentRepository;

	EquipmentDataInitializer(EquipmentRepository equipmentRepository){
		this.equipmentRepository = equipmentRepository;
		Equipment equipment1 = new Equipment("Große Bühne", Money.of(9.99, EURO), 10, 12, EquipmentType.STAGE);
		Equipment equipment2 = new Equipment("Kleine Bühne", Money.of(12.99, EURO), 10, 12, EquipmentType.STAGE);
		System.out.println("Equipmentid:" + equipment1.getId());
		equipmentRepository.save(equipment1);
		equipmentRepository.save(equipment2);
	}

	@Override
	public void initialize() {

//		if (equipmentRepository.findAll().iterator().hasNext()){
//			return;
//		}

		//LOG.info("Create default catalog entries");

		//equipmentRepository.save(new Equipment("stage1", Equipment.EquipmentType.STAGE));
	}
}
