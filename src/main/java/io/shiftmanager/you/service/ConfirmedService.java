package io.shiftmanager.you.service;

import io.shiftmanager.you.mapper.ConfirmedMapper;
import io.shiftmanager.you.model.Confirmed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ConfirmedService {

    private final ConfirmedMapper confirmedMapper;

    @Autowired
    public ConfirmedService(ConfirmedMapper confirmedMapper) {
        this.confirmedMapper = confirmedMapper;
    }

    public Confirmed getConfirmedById(Long id) {
        return confirmedMapper.getConfirmedById(id);
    }

    public List<Confirmed> getConfirmedByUserId(Long userId) {
        return confirmedMapper.getConfirmedByUserId(userId);
    }

    public Confirmed createConfirmed(Confirmed confirmed) {
        confirmedMapper.insertConfirmed(confirmed);
        return confirmed;
    }

    public void deleteConfirmed(Long id) {
        confirmedMapper.deleteConfirmed(id);
    }
}