package net.lucenews3.opensearch;


public class OpenSearchParameter {

	private String name;

	private String value;

	private Integer minimum;

	// TODO
	@SuppressWarnings("unused")
	private Boolean minimumUnbounded;

	private Integer maximum;

	// TODO
	@SuppressWarnings("unused")
	private Boolean maximumUnbounded;

	public OpenSearchParameter() {
	}

	public OpenSearchParameter(String name, String value) {
		setName(name);
		setValue(value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getMinimum() {
		return minimum;
	}

	public void setMinimum(Integer minimum) {
		this.minimum = minimum;
	}

	public void setMinimum(String minimum) {
		if (minimum.equals("*")) {
			minimumUnbounded = true;
			setMinimum((Integer) null);
		} else {
			minimumUnbounded = false;
			setMinimum(Integer.valueOf(minimum));
		}
	}

	public Integer getMaximum() {
		return maximum;
	}

	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
	}

	public void setMaximum(String maximum) {
		if (maximum.equals("*")) {
			maximumUnbounded = true;
			setMaximum((Integer) null);
		} else {
			maximumUnbounded = false;
			setMaximum(Integer.valueOf(maximum));
		}
	}

	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}

		if (other == this) {
			return true;
		}

		if (!(other instanceof OpenSearchParameter)) {
			return false;
		}

		OpenSearchParameter parameter = (OpenSearchParameter) other;

		if (parameter.getName() == null) {
			return false;
		}

		if (getName() == null) {
			return false;
		}

		if (getName().equals(parameter.getName())) {
			return true;
		}

		return false;
	}
}
