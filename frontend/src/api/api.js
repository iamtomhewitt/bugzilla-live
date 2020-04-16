import * as config from '../config';

export async function getBugs(numbers) {
	return fetch(config.backendUrl + '/bugs/numbers?numbers=' + numbers)
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

export async function deleteList(name) {
	return fetch(config.backendUrl + '/lists/delete?listName=' + name, {
		method: 'DELETE'
	})
		.then(response => response.json())
}

export async function updateCurrentList(list) {
	return fetch(config.backendUrl + '/lists/current', {
		method: 'PUT',
		body: JSON.stringify(list),
		headers: {
			'Content-Type': 'application/json'
		},
	})
		.then(response => response.json())
}

export async function updateList(list) {
	return fetch(config.backendUrl + '/lists/update', {
		method: 'PUT',
		body: JSON.stringify(list),
		headers: {
			'Content-Type': 'application/json'
		},
	})
		.then(response => response.json())
}