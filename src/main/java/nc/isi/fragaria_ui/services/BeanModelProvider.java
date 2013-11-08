package nc.isi.fragaria_ui.services;

import java.util.concurrent.ExecutionException;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class BeanModelProvider {

	@Inject
	private BeanModelBuilder beanModelBuilder;

	@Inject
	private Messages messages;

	private final LoadingCache<InnerModelIdentifier, BeanModel<?>> editModelCache = CacheBuilder
			.newBuilder().build(
					new CacheLoader<InnerModelIdentifier, BeanModel<?>>() {

						@Override
						public BeanModel<?> load(InnerModelIdentifier key)
								throws Exception {
							return beanModelBuilder.createEditModel(
									key.getType(), messages, key.getModelName());
						}

					});

	public BeanModel<?> getModel(Class<?> type, String modelName)
			throws ExecutionException {
		return editModelCache.get(new InnerModelIdentifier(modelName, type));
	}

	private class InnerModelIdentifier {
		private final String modelName;
		private final Class<?> type;

		public InnerModelIdentifier(String modelName, Class<?> type) {
			this.modelName = modelName;
			this.type = type;
		}

		public String getModelName() {
			return modelName;
		}

		public Class<?> getType() {
			return type;
		}

	}
}
