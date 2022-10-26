package in.mdbproject.Springbootmongodb.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.mdbproject.Springbootmongodb.exception.TodoCollectionException;
import in.mdbproject.Springbootmongodb.model.TodoDto;
import in.mdbproject.Springbootmongodb.repository.TodoRepository;
import in.mdbproject.Springbootmongodb.service.TodoService;

@RestController
public class TodoController {

	@Autowired
	private TodoRepository todorepo;

	@Autowired
	private TodoService todoService;

	@GetMapping("/todos")
	public ResponseEntity<?> getAlltodos() {
		List<TodoDto> todo = todoService.getAllTodos();
		return new ResponseEntity<>(todo, todo.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
//		if (todo.size()>0) {
//			return new ResponseEntity<List<TodoDto>>(todo, HttpStatus.OK);
//		}else {
//			return new ResponseEntity<>("No todos available", HttpStatus.OK);
//		}
	}

	@PostMapping("/todos")
	public ResponseEntity<?> createdTodo(@RequestBody TodoDto dto) throws TodoCollectionException {
		try {
			todoService.createTodo(dto);
			return new ResponseEntity<TodoDto>(dto, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (TodoCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/todos/{id}")
	public ResponseEntity<?> getSingleTodo(@PathVariable("id") String id) {
		try {
			return new ResponseEntity<>(todoService.getSingleTodo(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}

//		Optional<TodoDto> todoOptional = todorepo.findById(id);
//		if (todoOptional.isPresent()) {
//			return new ResponseEntity<>(todoOptional.get(), HttpStatus.OK);
//		}else {
//			return new ResponseEntity<>("Todo not found with id "+id, HttpStatus.OK);
//		}
	}

	@PutMapping("/todos/{id}")
	public ResponseEntity<?> updateById(@PathVariable("id") String id, @RequestBody TodoDto todo) {
		try {
			todoService.todoUpdate(id, todo);
			return new ResponseEntity<>("Upadated todo with id " + id, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (TodoCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}

//		if (todoOptional.isPresent()) {
//			TodoDto todoToSave = todoOptional.get();
//			todoToSave.setCompleted(todo.isCompleted() != false ? todo.isCompleted() : todoToSave.isCompleted());
//			todoToSave.setTodo(todo.getTodo() != null ? todo.getTodo() : todoToSave.getTodo());
//			todoToSave.setDescription(todo.getDescription() != null ? todo.getDescription() : todoToSave.getDescription());
//			todoToSave.setUpdatedAt(new Date(System.currentTimeMillis()));
//			todorepo.save(todoToSave);
//			return new ResponseEntity<>(todoToSave, HttpStatus.OK);
//		}else {
//			return new ResponseEntity<>("Todo not found with id "+id, HttpStatus.NOT_FOUND);
//		}
	}

	@DeleteMapping("/todos/{id}")
	public ResponseEntity<?> deleteById(@PathVariable("id") String id) {
		try {
//			todorepo.deleteById(id);
			todoService.deleteTodoById(id);
			return new ResponseEntity<>("Successfully deleted by id " + id, HttpStatus.OK);
		} catch (TodoCollectionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

}
