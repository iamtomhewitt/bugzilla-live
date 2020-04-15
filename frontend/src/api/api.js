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

export async function getLists() {
	return fetch(config.backendUrl + '/lists/all')
		.then(response => response.json())
}

export async function getCurrentList() {
	return fetch(config.backendUrl + '/lists/current')
		.then(response => response.json())
}