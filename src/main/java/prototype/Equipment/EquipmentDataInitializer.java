//package prototype.Equipment;
//
//import org.javamoney.moneta.Money;
//import org.salespointframework.core.DataInitializer;
//import org.slf4j.LoggerFactory;
//
//import org.slf4j.Logger;
//
//import static org.salespointframework.core.Currencies.EURO;
//
//public class EquipmentDataInitializer implements DataInitializer {
//
//	private static final Logger LOG= LoggerFactory.getLogger(EquipmentDataInitializer.class);
//
//	private final EquipmentCatalog equipmentCatalog;
//
//	EquipmentDataInitializer(EquipmentCatalog equipmentCatalog){
//		this.equipmentCatalog=equipmentCatalog;
//	}
//
//	@Override
//	public void initialize() {
//
//		if (equipmentCatalog.findAll().iterator().hasNext()){
//			return;
//		}
//
//		LOG.info("Create default catalog entries");
//
//		equipmentCatalog.save(new Equipment("stage1", Money.of(9.99, EURO), Equipment.EquipmentType.STAGE));
//	}
//}
