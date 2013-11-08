package nc.isi.fragaria_ui.utils.events.modalbeaneditform;

import nc.isi.fragaria_ui.utils.events.AbstractObjectEvent;

public class DisplayEvent<T> extends AbstractObjectEvent<T> {

	private final Boolean editable;

	public DisplayEvent(T object, Boolean editable) {
		super(object);
		this.editable = editable;
	}

	public Boolean getEditable() {
		return editable;
	}
}
