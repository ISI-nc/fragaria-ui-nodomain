package nc.isi.fragaria_ui.services;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Collection;
import java.util.Map;

import nc.isi.fragaria_reflection.services.ObjectMetadataProvider;
import nc.isi.fragaria_reflection.utils.ObjectMetadata;

import org.apache.log4j.Logger;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.BeanModelSource;

@SuppressWarnings("rawtypes")
public class BeanModelBuilderImpl implements BeanModelBuilder {
	private static final Logger LOGGER = Logger
			.getLogger(BeanModelBuilderImpl.class);
	private final BeanModelSource beanModelSource;
	private final ObjectMetadataProvider objectMetadataProvider;
	private final Map<String, BeanModelDefinition> beanModelDefinitions;

	public BeanModelBuilderImpl(BeanModelSource beanModelSource,
			Map<String, BeanModelDefinition> beanModelDefinitions,
			ObjectMetadataProvider objectMetadataProvider) {
		super();
		this.beanModelSource = beanModelSource;
		this.beanModelDefinitions = beanModelDefinitions;
		this.objectMetadataProvider = objectMetadataProvider;
	}

	@Override
	public <T> BeanModel<T> createDisplayModel(Class<T> type,
			Messages messages, String name) {
		BeanModel<T> beanModel = beanModelSource.createDisplayModel(type,
				messages);
		updateBeanModel(type, name, beanModel);
		return beanModel;
	}

	@SuppressWarnings("unchecked")
	protected <T> void updateBeanModel(Class<T> type, String name,
			BeanModel<T> beanModel) {
		if (name != null) {
			BeanModelDefinition<T> beanModelDefinition = (BeanModelDefinition<T>) beanModelDefinitions
					.get(name);
			if (beanModelDefinition != null) {
				checkArgument(beanModelDefinition != null,
						"aucune beanModelDefinition trouvée pour : %s", type);
				LOGGER.info("beanModelDefinition trouvée pour " + type + " : "
						+ beanModelDefinition.name());
				Class<?> beanModelClass = type;
				while (beanModelClass != null
						&& beanModelClass != beanModelDefinition.beanClass()) {
					beanModelClass = beanModelClass.getSuperclass();
				}
				checkArgument(
						beanModelClass == beanModelDefinition.beanClass(),
						"la classe du beanModelDefinition ne correspond pas à la classe où à une une"
								+ "superclasse de : %s", type);
				ObjectMetadata objectMetadata = objectMetadataProvider
						.provide(type);
				if (beanModelDefinition.exclude() != null) {
					beanModel.exclude(beanModelDefinition.exclude());
				}
				if (beanModelDefinition.add() != null) {
					for (String property : beanModelDefinition.add()) {
						String dataType = Collection.class
								.isAssignableFrom(objectMetadata
										.propertyType(property)) ? property
								: objectMetadata.propertyType(property)
										.getSimpleName();
						beanModel.add(property).dataType(dataType);
					}
				}
				if (beanModelDefinition.reOrder() != null) {
					beanModel.reorder(beanModelDefinition.reOrder());
				}
			} else {
				LOGGER.info("aucune beanModelDefinition trouvée pour : " + name
						+ " un modèle de base sera généré automatiquement");
			}
		}
	}

	@Override
	public <T> BeanModel<T> createEditModel(Class<T> type, Messages messages,
			String name) {
		BeanModel<T> beanModel = beanModelSource
				.createEditModel(type, messages);
		updateBeanModel(type, name, beanModel);
		return beanModel;
	}

}
