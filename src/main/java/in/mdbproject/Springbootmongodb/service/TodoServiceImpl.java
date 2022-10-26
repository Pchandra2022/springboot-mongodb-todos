package in.mdbproject.Springbootmongodb.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.MethodInvocationRecorder.Recorded.ToCollectionConverter;
import org.springframework.stereotype.Service;

import in.mdbproject.Springbootmongodb.exception.TodoCollectionException;
import in.mdbproject.Springbootmongodb.model.TodoDto;
import in.mdbproject.Springbootmongodb.repository.TodoRepository;

@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoRepository todoRepo;

	@Override
	public void createTodo(TodoDto todo) throws ConstraintViolationException, TodoCollectionException {
		Optional<TodoDto> todoOptional = todoRepo.findByTodo(todo.getTodo());
		if (todoOptional.isPresent()) {
			throw new TodoCollectionException(TodoCollectionException.TodoAlreadyExists());
		} else {
			todo.setCreatedAt(new Date(System.currentTimeMillis()));
			todoRepo.save(todo);
		}
	}

	@Override
	public List<TodoDto> getAllTodos() {
		List<TodoDto> todos = todoRepo.findAll();
		if (todos.size() > 0) {
			return todos;
		} else {
			return new ArrayList<TodoDto>();
		}
	}

	@Override
	public TodoDto getSingleTodo(String id) throws TodoCollectionException {
		Optional<TodoDto> optionalTodo = todoRepo.findById(id);
		if (!optionalTodo.isPresent()) {
			throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
		} else {
			return optionalTodo.get();
		}
	}

	@Override
	public void todoUpdate(String id, TodoDto todoDto) throws TodoCollectionException {
		Optional<TodoDto> todoWithId = todoRepo.findById(id);
		Optional<TodoDto> todoWithSameName = todoRepo.findByTodo(id);
		if (todoWithId.isPresent()) {
			if (todoWithSameName.isPresent() && !todoWithSameName.get().getId().equals(id)) {
				throw new TodoCollectionException(TodoCollectionException.TodoAlreadyExists());
			}
			TodoDto todoToUpadate = todoWithId.get();

			todoToUpadate.setTodo(todoDto.getTodo());
			todoToUpadate.setDescription(todoDto.getDescription());
			todoToUpadate.setCompleted(todoDto.isCompleted());
			todoToUpadate.setUpdatedAt(todoDto.getUpdatedAt());
			todoRepo.save(todoToUpadate);
		} else {
			throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
		}
	}

	@Override
	public void deleteTodoById(String id) throws TodoCollectionException {
		Optional<TodoDto> todoOptional = todoRepo.findById(id);
		if (!todoOptional.isPresent()) {
			throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
		}else {
			todoRepo.deleteById(id);
		}
	}

}
