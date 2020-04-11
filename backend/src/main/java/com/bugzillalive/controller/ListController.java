package com.bugzillalive.controller;

import com.bugzillalive.config.mongo.UserConfig;
import com.bugzillalive.exception.ConfigNotFoundException;
import com.bugzillalive.exception.ListNotFoundException;
import com.bugzillalive.exception.NoCurrentListException;
import com.bugzillalive.model.BugList;
import com.bugzillalive.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("lists")
@CrossOrigin(origins="*")
public class ListController {

	@Autowired
	private ListService listService;

	@GetMapping("/health")
	public ResponseEntity<String> health() {
		return new ResponseEntity<>("UP", HttpStatus.OK);
	}

	@GetMapping("/{listName}")
	public ResponseEntity<BugList> getList(@PathVariable String listName) throws ListNotFoundException {
		return new ResponseEntity<>(listService.getList(listName), HttpStatus.OK);
	}

	@GetMapping("/all")
	public ResponseEntity<List<BugList>> getAllList() {
		return new ResponseEntity<>(listService.getBugLists(), HttpStatus.OK);
	}

	@GetMapping("/current")
	public ResponseEntity<BugList> getCurrentList() throws NoCurrentListException {
		return new ResponseEntity<>(listService.getCurrentList(), HttpStatus.OK);
	}

	@PostMapping("/save")
	public ResponseEntity<UserConfig> saveList(@RequestBody BugList list) throws ConfigNotFoundException {
		return new ResponseEntity<>(listService.saveList(list), HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<UserConfig> updateList(@RequestBody BugList list) throws ConfigNotFoundException {
		return new ResponseEntity<>(listService.updateList(list), HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<UserConfig> deleteList(@RequestParam String listName) throws ConfigNotFoundException {
		return new ResponseEntity<>(listService.deleteList(listName), HttpStatus.OK);
	}
}