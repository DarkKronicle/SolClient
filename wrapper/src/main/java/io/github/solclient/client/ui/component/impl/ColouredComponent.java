package io.github.solclient.client.ui.component.impl;

import io.github.solclient.client.ui.component.Component;
import io.github.solclient.client.ui.component.controller.Controller;
import io.github.solclient.client.util.data.Colour;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ColouredComponent extends Component {

	protected final Controller<Colour> colour;

	public Colour getColour() {
		return colour.get(this, theme.fg);
	}

	public int getColourValue() {
		return getColour().getValue();
	}

}
