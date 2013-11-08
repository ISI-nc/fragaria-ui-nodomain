package nc.isi.fragaria_ui.services;


public interface BeanModelDefinition<T> {

	String name();

	Class<T> beanClass();

	String[] add();

	String[] reOrder();

	String[] exclude();

}
