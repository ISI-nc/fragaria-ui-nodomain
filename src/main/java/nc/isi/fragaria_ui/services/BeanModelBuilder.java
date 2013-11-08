package nc.isi.fragaria_ui.services;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;

public interface BeanModelBuilder {

	public <T> BeanModel<T> createDisplayModel(Class<T> type,
			Messages messages, String name);

	public <T> BeanModel<T> createEditModel(Class<T> type, Messages messages,
			String name);

}