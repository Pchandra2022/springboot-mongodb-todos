package in.mdbproject.Springbootmongodb.service;

import java.util.List;

import javax.validation.ConstraintViolationException;

import in.mdbproject.Springbootmongodb.exception.TodoCollectionException;
import in.mdbproject.Springbootmongodb.model.TodoDto;

public interface TodoService {

	public void createTodo(TodoDto todo) throws ConstraintViolationException, TodoCollectionException;
	
	public List<TodoDto> getAllTodos();
	
	public TodoDto getSingleTodo(String id) throws TodoCollectionException;
	
	public void todoUpdate(String id, TodoDto todoDto) throws TodoCollectionException;
	
	public void deleteTodoById(String id) throws TodoCollectionException;
	
}
