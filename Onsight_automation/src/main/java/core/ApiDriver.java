package core;

import io.restassured.specification.RequestSpecification;

public interface ApiDriver {
	/**
	 * Initializes an API request with the given base URL.
	 *
	 * @param baseUrl The base URL for the API.
	 * @return A RequestSpecification instance for API interactions.
	 */
	RequestSpecification apiInit(String baseUrl);
}

