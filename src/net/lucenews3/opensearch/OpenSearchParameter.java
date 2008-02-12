package net.lucenews3.opensearch;


public class OpenSearchParameter {

	private String name;
	private String value;
	private Integer minimum;
	private Integer maximum;

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

	public Integer getMaximum() {
		return maximum;
	}

	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
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
