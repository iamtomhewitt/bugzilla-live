import * as config from '../config';

export async function getBugs(numbers) {
	let commaList = numbers.join(",")

	return fetch(config.backendUrl + '/bugs/numbers?numbers=' + commaList)
		.then(response => response.json())
}

export async function getConfig() {
	return fetch(config.backendUrl + '/config/get')
		.then(response => response.json())
}