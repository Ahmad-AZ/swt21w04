package festivalmanager.Equipment;

import static org.salespointframework.core.Currencies.EURO;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import festivalmanager.Equipment.Equipment.EquipmentType;

/**
 * Initializes default equipments
 *
 * @author Tuan Giang Trinh
 */
@Component
@Order(10)
public class EquipmentDataInitializer implements DataInitializer {

	private static final Logger LOG= LoggerFactory.getLogger(EquipmentDataInitializer.class);

	private EquipmentRepository equipmentRepository;
	/**
	 * Create a new {@link EquipmentDataInitializer} and add new {@link Equipment}
	 * @param equipmentRepository
	 */
	EquipmentDataInitializer(EquipmentRepository equipmentRepository){
		this.equipmentRepository = equipmentRepository;
	}

	@Override
	public void initialize() {
		if(equipmentRepository.findAll().iterator().hasNext()) {
			return;
		}
		LOG.info("Creating default equipment entries.");
		Equipment equipment1 = new Equipment("Bühne", Money.of(300.00, EURO), EquipmentType.STAGE);
		Equipment equipment2 = new Equipment("Toilette", Money.of(75.00, EURO), EquipmentType.TOILET);
		Equipment equipment3 = new Equipment("Imbissstand", Money.of(150.00, EURO), EquipmentType.CATERING_STALL);
		equipmentRepository.save(equipment1);
		equipmentRepository.save(equipment2);
		equipmentRepository.save(equipment3);
	}
}
