package dasturlash.uz.service;

import dasturlash.uz.dto.TaskDTO;
import dasturlash.uz.entity.TaskEntity;
import dasturlash.uz.enums.ProfileRole;
import dasturlash.uz.exp.AppBadRequestException;
import dasturlash.uz.exp.ItemNotFoundException;
import dasturlash.uz.repository.TaskRepository;
import dasturlash.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    private List<TaskDTO> taskList;

    public TaskDTO create(TaskDTO dto) {
        TaskEntity entity = new TaskEntity();
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setProfileId(SpringSecurityUtil.getCurrentProfileId());

        taskRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public List<TaskDTO> getAll() {
        Iterable<TaskEntity> taskList = taskRepository.findAll();
        List<TaskDTO> dtoList = new LinkedList<>();
        for (TaskEntity task : taskList) {
            dtoList.add(toDTO(task));
        }
        return dtoList;
    }

    public List<TaskDTO> getCurrentProfileTaskList() {
        String profileId = SpringSecurityUtil.getCurrentProfileId();
        List<TaskEntity> taskList = taskRepository.findAllByProfileId(profileId);
        return taskList.stream().map(entity -> toDTO(entity)).collect(Collectors.toList());
    }

    public TaskDTO getById(String id) {
        TaskEntity entity = get(id);
        return toDTO(entity);
    }

    public Boolean update(TaskDTO dto, String id) {
        TaskEntity entity = get(id);

        String userId = SpringSecurityUtil.getCurrentProfileId();
        if (!entity.getProfileId().equals(userId)) {
            throw new AppBadRequestException("It does not belong to the current profile. Mazgi.");
        }
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        taskRepository.save(entity);
        return true;
    }

    public Boolean delete(String id) {
        TaskEntity entity = get(id);
        String userId = SpringSecurityUtil.getCurrentProfileId();
        List<String> roleList = SpringSecurityUtil.getProfileRoleList();

        if (!entity.getProfileId().equals(userId) &&
                !roleList.contains(ProfileRole.ROLE_ADMIN.name())) {
            throw new AppBadRequestException("It does not belong to the current profile. Mazgi.");
        }

        taskRepository.delete(entity);
        return true;
    }

    public Boolean deleteAsAdmin(String id) {
        taskRepository.deleteById(id);
        return true;
    }


    public TaskEntity get(String id) {
        return taskRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("Task not found");
        });
    }

    public TaskDTO toDTO(TaskEntity entity) {
        TaskDTO dto = new TaskDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
}
