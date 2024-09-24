package dasturlash.uz.controller;

import dasturlash.uz.dto.TaskDTO;
import dasturlash.uz.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping("")
    public ResponseEntity<TaskDTO> create(@RequestBody TaskDTO dto) {
        TaskDTO result = taskService.create(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping({"", "/"}) // /task    /task/
    public ResponseEntity<List<TaskDTO>> getAll() {
        List<TaskDTO> result = taskService.getAll();
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/my")
    public ResponseEntity<List<TaskDTO>> getMyTaskList() {
        List<TaskDTO> result = taskService.getCurrentProfileTaskList();
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/finished/all")
    public ResponseEntity<List<TaskDTO>> getFinishedAll() {
        List<TaskDTO> result = taskService.getCurrentProfileTaskList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getById(@PathVariable String id) {
        TaskDTO result = taskService.getById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Boolean> update(@RequestBody TaskDTO student,
                                          @PathVariable("id") String id) {
        Boolean result = taskService.update(student, id);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        taskService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/admin")
    public ResponseEntity<Void> deleteAsAdmin(@PathVariable("id") String id) {
        taskService.deleteAsAdmin(id);
        return ResponseEntity.ok().build();
    }
}
