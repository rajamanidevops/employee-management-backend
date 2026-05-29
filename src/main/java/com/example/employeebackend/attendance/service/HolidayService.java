package com.example.employeebackend.attendance.service;

import com.example.employeebackend.attendance.entity.Holiday;
import com.example.employeebackend.attendance.repository.HolidayRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HolidayService {

    private final HolidayRepository holidayRepository;

    public HolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    /* Get all holidays */
    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    /* Save holiday (avoid duplicates) */
    public Holiday addHoliday(Holiday holiday) {
        if (holidayRepository.existsByHolidayDate(holiday.getHolidayDate())) {
            throw new RuntimeException("Holiday already exists for date: " + holiday.getHolidayDate());
        }
        return holidayRepository.save(holiday);
    }

    /* Check if a date is holiday */
    public boolean isHoliday(LocalDate date) {
        return holidayRepository.existsByHolidayDate(date);
    }
}