package prototype.Equipment;


import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;

public interface EquipmentCatalog extends Catalog<Equipment> {

	static final Sort DEFAULT_SORT = Sort.by("equipmentIdetifier").descending();

	Iterable <Equipment> findByType(Equipment.EquipmentType equipmentType, Sort sort);

	default Iterable <Equipment> findByType(Equipment.EquipmentType equipmentType){
		return findByType(equipmentType, DEFAULT_SORT);
	}
}
