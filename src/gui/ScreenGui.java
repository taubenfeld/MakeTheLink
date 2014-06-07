package gui;

import java.util.Map;

public interface ScreenGui {

	void initialize(Map<String, Integer> playersAndKeys);

	void setLayout();

	void makeWidgets();
}
