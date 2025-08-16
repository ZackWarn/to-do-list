import com.example.to_dolist.todo

object todoManager {
    private val todoList = mutableListOf<todo>()
    private var nextId = 1

    fun getallTodo(): List<todo> {
        return todoList
    }

    fun addTodo(title: String) {
        todoList.add(todo(nextId++, title, System.currentTimeMillis()))
    }

    fun deleteTodo(id: Int) {
        todoList.removeIf { it.id == id }
    }
}
