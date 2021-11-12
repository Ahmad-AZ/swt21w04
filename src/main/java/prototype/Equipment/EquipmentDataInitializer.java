package prototype.Equipment;

import org.salespointframework.core.DataInitializer;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

public class EquipmentDataInitializer implements DataInitializer {

	private static final Logger LOG= LoggerFactory.getLogger(EquipmentDataInitializer.class);

	private final EquipmentCatalog equipmentCatalog;

	EquipmentDataInitializer(EquipmentCatalog equipmentCatalog){
		this.equipmentCatalog=equipmentCatalog;
	}

	@Override
	public void initialize() {

		if (equipmentCatalog.findAll().iterator().hasNext()){
			return;
		}

		LOG.info("Create default catalog entries");

		equipmentCatalog.save(new Equipment("stage1", Equipment.EquipmentType.STAGE));
	}
}
