package com.internsystem.internmanagement.service;

import com.internsystem.internmanagement.entity.Intern;
import com.internsystem.internmanagement.exception.ResourceNotFoundException;
import com.internsystem.internmanagement.repository.InternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InternService {

    @Autowired
    private InternRepository internRepository;

    public List<Intern> getAllInterns() {
        return internRepository.findAll();
    }

    public Intern createIntern(Intern intern) {
        return internRepository.save(intern);
    }

    public Optional<Intern> getInternById(Long id) {
        return internRepository.findById(id);
    }

    public Intern updateIntern(Long id, Intern updatedIntern) {
        Intern intern = internRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intern not found with ID: " + id));

        intern.setName(updatedIntern.getName());
        intern.setEmail(updatedIntern.getEmail());
        intern.setPhone(updatedIntern.getPhone());
        intern.setTrainingStartDate(updatedIntern.getTrainingStartDate());
        intern.setTrainingEndDate(updatedIntern.getTrainingEndDate());
        intern.setInstitute(updatedIntern.getInstitute());

        return internRepository.save(intern);
    }

    public void deleteIntern(Long id) {
        if (!internRepository.existsById(id)) {
            throw new ResourceNotFoundException("Intern not found with ID: " + id);
        }
        internRepository.deleteById(id);
    }
}
